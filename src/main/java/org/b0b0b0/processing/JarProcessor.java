package org.b0b0b0.processing;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JarProcessor {

    public static final String INPUT_DIRECTORY = "input";
    public static final String OUTPUT_DIRECTORY = "output";
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    public void processJars() {
        File inputDirectory = new File(INPUT_DIRECTORY);
        boolean isNewDirectory = false;

        if (!inputDirectory.exists()) {
            if (inputDirectory.mkdirs()) {
                System.out.println(BOLD + GREEN + "\nПапка для входных файлов создана: " + INPUT_DIRECTORY + RESET);
                System.out.println(CYAN + "Пожалуйста, добавьте JAR-файлы в папку 'input' и запустите программу снова." + RESET);
                isNewDirectory = true;
            } else {
                System.out.println(RED + "\nНе удалось создать папку для входных файлов: " + INPUT_DIRECTORY + RESET);
                return;
            }
        }

        if (isNewDirectory) {
            // Если папка только что создана, ждем добавления файлов
            return;
        }

        List<File> jarFiles = DirectoryManager.getJarFiles(inputDirectory);

        if (jarFiles.isEmpty()) {
            System.out.println(YELLOW + "\nВ папке 'input' не найдено JAR-файлов." + RESET);
            return;
        }

        File outputDirectory = DirectoryManager.createDirectory(OUTPUT_DIRECTORY);
        System.out.println(BLUE + "\nНайдено " + jarFiles.size() + " JAR-файлов. Начинаем обработку..." + RESET);

        for (File jarFile : jarFiles) {
            String outputFileName = jarFile.getName();
            File outputJar = new File(outputDirectory, outputFileName);
            int counter = 1;

            // Проверяем, существует ли файл с таким именем, и уникализируем его
            while (outputJar.exists()) {
                String newFileName = jarFile.getName().replace(".jar", "") + "_" + counter + ".jar";
                outputJar = new File(outputDirectory, newFileName);
                counter++;
            }

            try {
                ClassProcessor.processJar(jarFile, outputJar);
                System.out.println(GREEN + "Обработан файл: " + jarFile.getName() + RESET);
                System.out.println(GREEN + "Сохранён как: " + outputJar.getName() + RESET);
            } catch (IOException e) {
                System.out.println(RED + "Ошибка обработки файла: " + jarFile.getName() + RESET);
                e.printStackTrace();
            }
        }

        System.out.println(BOLD + GREEN + "\nОбработка завершена. Обработанные файлы сохранены в папку 'output'." + RESET);
    }
}
