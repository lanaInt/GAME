package src.main.java;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class Main {
    public static void main(String[] args) {
        StringBuilder logTxt = new StringBuilder();
        List<String> listDir = new ArrayList<>(List.of(
                "D:\\work\\Netology\\Game\\New\\src",
                "D:\\work\\Netology\\Game\\New\\res",
                "D:\\work\\Netology\\Game\\New\\savegames",
                "D:\\work\\Netology\\Game\\New\\temp",
                "D:\\work\\Netology\\Game\\New\\src\\main",
                "D:\\work\\Netology\\Game\\New\\src\\test",
                "D:\\work\\Netology\\Game\\New\\res\\drawables",
                "D:\\work\\Netology\\Game\\New\\res\\vectors",
                "D:\\work\\Netology\\Game\\New\\res\\icons"));
        for (String path: listDir) {
            createDir(path,logTxt);
        }

        //В папке Games создайте несколько директорий: src, res, savegames, temp.
        //В каталоге src создайте две директории: main, test.
        //В каталог res создайте три директории: drawables, vectors, icons.
        //В подкаталоге main создайте два файла: Main.java, Utils.java.
        List<String> listFile = new ArrayList<>(List.of(
                "D:\\work\\Netology\\Game\\New\\src\\main\\Main.java",
                "D:\\work\\Netology\\Game\\New\\src\\main\\Utils.java",
                "D:\\work\\Netology\\Game\\New\\temp\\temp.txt"));
        for (String pathFile: listFile) {
            createFile(pathFile,logTxt);
        }

        //В директории temp создайте файл temp.txt.
        String fileTemp = "D:\\work\\Netology\\Game\\New\\temp\\temp.txt";

        // Теперь записываем накопленный лог в файл temp.txt с помощью FileWriter
        try (FileWriter writer = new FileWriter(fileTemp);) {
            writer.write(logTxt.toString());
            // Можно добавить writer.flush() для гарантии записи, но try-with-resources делает это автоматически
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
        }
        //Сохранение
        // Создать три экземпляра класса GameProgress
        GameProgress gameProgress1 = new GameProgress(64, 8, 3, 134.30);
        GameProgress gameProgress2 = new GameProgress(85, 5, 2, 204.52);
        GameProgress gameProgress3 = new GameProgress(77, 10, 2, 153.66);
        //Сохранить сериализованные объекты GameProgress в папку savegames из предыдущей задачи.
        saveGame("D:\\work\\Netology\\Game\\New\\savegames\\save1.dat",gameProgress1);
        saveGame("D:\\work\\Netology\\Game\\New\\savegames\\save2.dat",gameProgress2);
        saveGame("D:\\work\\Netology\\Game\\New\\savegames\\save3.dat",gameProgress3);
        //Созданные файлы сохранений из папки savegames запаковать в один архив zip.
        //Удалить файлы сохранений, лежащие вне архива.
        List<String> filePaths = List.of(new String[]{"D:\\work\\Netology\\Game\\New\\savegames\\save1.dat", "D:\\work\\Netology\\Game\\New\\savegames\\save2.dat", "D:\\work\\Netology\\Game\\New\\savegames\\save3.dat"});
        //System.out.println(filePaths);
        zipFiles("D:\\work\\Netology\\Game\\New\\savegames\\zip.zip",filePaths);
    }

    private static void createDir (String pathDir, StringBuilder logTxt) {
        File file = new File(pathDir);
        if (file.mkdir()) {
            logTxt.append("Директория успешно создана: ").append(file.getAbsolutePath()).append(System.lineSeparator());
        } else {
            logTxt.append("Не удалось создать директорию. Возможно, она уже существует или есть другие проблемы.").append(file.getAbsolutePath()).append(System.lineSeparator());
        }
    }

    private static void createFile (String pathFile, StringBuilder logTxt) {
        File fileName = new File(pathFile);
        try {
            if (fileName.createNewFile()) {
                logTxt.append("Файл создан ").append(fileName.getAbsoluteFile()).append(System.lineSeparator());
            } else {
                logTxt.append("Файл уже существовал ").append(fileName.getAbsoluteFile()).append(System.lineSeparator());
            }
        } catch (IOException e) {
            logTxt.append("Ошибка при создании файла: ").append(e.getMessage()).append(System.lineSeparator());
        }
    }

    private static void saveGame(String pathFile, GameProgress progressGame) {
    // откроем выходной поток для записи в файл
        try (FileOutputStream fos = new FileOutputStream(pathFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            // запишем экземпляр класса в файл
            oos.writeObject(progressGame);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    private static void zipFiles(String pathZip, List<String> filePaths) {
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(pathZip))) {
            for (String filePath: filePaths) {
                try (FileInputStream fileIn = new FileInputStream(filePath)) {
                    File file = new File(filePath);
                    String fileName = file.getName();
                    ZipEntry zipEntry = new ZipEntry(fileName);
                    zipOut.putNextEntry(zipEntry);

                    byte[] buffer = new byte[fileIn.available()];
                    fileIn.read(buffer);
                    // добавляем содержимое к архиву
                    zipOut.write(buffer);
                    zipOut.closeEntry();
                } catch (IOException e) {
                    System.err.println("Ошибка при чтении файла:" + filePath + "-" + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка создания архива:" + e.getMessage());
        }

        // Удаление исходных файлов после упаковки
        for (String filePath: filePaths) {
            File file = new File(filePath);
            if (file.exists() && file.delete()) {
                System.out.println("Файл удалён:" + filePath);
            } else {
                System.err.println("Не удалось удалить файл:" + filePath);
            }
        }
    }
}
