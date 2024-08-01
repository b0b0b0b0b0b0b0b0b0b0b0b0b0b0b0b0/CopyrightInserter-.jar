package org.b0b0b0.processing;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class CLI {

    public static void start() {
        boolean configCreated = ConfigLoader.isConfigNewlyCreated();
        if (configCreated) {
            System.out.println(" ");
            System.out.println("Файл config.yml был создан и позволяет настроить параметры обработки.");
            System.out.println("Вы можете изменить его при необходимости, но это не обязательно.");
            System.out.println(" ");
        }

        System.out.println("Запуск автоматической обработки JAR-файлов из папки 'input'...");
        autoProcessJars();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayMenu();
            System.out.print("> ");
            String command = scanner.nextLine().trim();

            switch (command) {
                case "1":
                    System.out.println(" ");
                    System.out.println("Запуск обработки JAR-файлов...");
                    autoProcessJars();
                    break;

                case "2":
                    System.out.println(" ");
                    System.out.println("Перезагрузка конфигурации...");
                    ConfigLoader.reloadConfig();
                    System.out.println("Конфигурация перезагружена.");
                    System.out.println(" ");
                    break;

                case "3":
                    System.out.println("Показать список доступных JAR-файлов...");
                    listAvailableJars();
                    break;

                case "4":
                    System.out.println("Показать содержимое файла config.yml...");
                    showConfigFile();
                    break;

                case "5":
                    System.out.println("Открытие папки 'input'...");
                    openDirectory("input");
                    break;

                case "6":
                    System.out.println("Открытие папки 'output'...");
                    openDirectory("output");
                    break;

                case "7":
                    System.out.println("Очистка папки 'output'...");
                    clearOutputDirectory();
                    break;

                case "8":
                    System.out.println("Обновление списка файлов...");
                    autoProcessJars();
                    break;

                case "9":
                    System.out.println("Отображение справки...");
                    displayHelp();
                    break;

                case "10":
                    System.out.println("Завершение работы программы...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Неизвестная команда. Пожалуйста, выберите номер из меню.");
                    break;
            }
        }
    }

    private static void displayMenu() {
        System.out.println(" ");
        System.out.println("\nПожалуйста, выберите действие:");
        System.out.println("1 - Запустить обработку файлов в папке 'input'");
        System.out.println("2 - Перезагрузка конфигурации");
        System.out.println("3 - Показать список доступных JAR-файлов");
        System.out.println("4 - Показать содержимое файла config.yml");
        System.out.println("5 - Открыть папку 'input'");
        System.out.println("6 - Открыть папку 'output'");
        System.out.println("7 - Очистить папку 'output'");
        System.out.println("8 - Обновить список файлов");
        System.out.println("9 - Справка");
        System.out.println("10 - Выход");
        System.out.println(" ");
    }

    private static void autoProcessJars() {
        File inputDirectory = DirectoryManager.createDirectory("input");
        List<File> jarFiles = DirectoryManager.getJarFiles(inputDirectory);

        if (jarFiles.isEmpty()) {
            String message = "\nПапка 'input' пуста. Пожалуйста, добавьте JAR-файлы для обработки.\n"
                    + "Наш инструмент предназначен для автоматического добавления метаданных в ваши JAR-файлы.\n"
                    + "Просто поместите нужные JAR-файлы в папку 'input', и мы сделаем всю работу за вас!\n"
                    + "После этого выберите опцию '1 - Запустить обработку файлов' для начала обработки.\n"
                    + "Если вам нужно перезагрузить конфигурацию, выберите '2'.\n"
                    + "Для выхода из программы выберите '10'.\n";
            printWithDelay(message);
            return;
        }

        for (File inputJar : jarFiles) {
            File outputJar = new File("output", inputJar.getName());
            outputJar.getParentFile().mkdirs();
            try {
                ClassProcessor.processJar(inputJar, outputJar);
                System.out.println("Донор файла: " + inputJar.getName());
            } catch (IOException e) {
                System.err.println("Ошибка при обработке файла: " + inputJar.getName());
                e.printStackTrace();
            }
        }
    }

    private static void listAvailableJars() {
        File inputDirectory = new File("input");
        List<File> jarFiles = DirectoryManager.getJarFiles(inputDirectory);

        if (jarFiles.isEmpty()) {
            System.out.println("Нет JAR-файлов в папке 'input'.");
            return;
        }

        System.out.println("Доступные JAR-файлы:");
        for (File jarFile : jarFiles) {
            System.out.println("- " + jarFile.getName());
        }
    }

    private static void showConfigFile() {
        try {
            List<String> lines = Files.readAllLines(Path.of("config.yml"));
            System.out.println("\nСодержимое файла config.yml:");
            lines.forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла config.yml.");
        }
    }

    private static void openDirectory(String dirName) {
        File directory = new File(dirName);
        if (directory.exists() && directory.isDirectory()) {
            try {
                Desktop.getDesktop().open(directory);
                System.out.println("Папка '" + dirName + "' открыта.");
            } catch (IOException e) {
                System.err.println("Ошибка при открытии папки '" + dirName + "'.");
            }
        } else {
            System.out.println("Папка '" + dirName + "' не найдена.");
        }
    }

    private static void clearOutputDirectory() {
        File outputDirectory = new File("output");
        if (outputDirectory.exists() && outputDirectory.isDirectory()) {
            File[] files = outputDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        if (file.delete()) {
                            System.out.println("Удален файл: " + file.getName());
                        } else {
                            System.out.println("Не удалось удалить файл: " + file.getName());
                        }
                    }
                }
            }
        } else {
            System.out.println("Папка 'output' не найдена.");
        }
    }

    private static void displayHelp() {
        String helpMessage = "Справка:\n"
                + "1 - Запустить обработку файлов в папке 'input': Начинает обработку JAR-файлов.\n"
                + "2 - Перезагрузка конфигурации: Перезагружает настройки из файла config.yml.\n"
                + "3 - Показать список доступных JAR-файлов: Отображает файлы в папке 'input'.\n"
                + "4 - Показать содержимое файла config.yml: Выводит настройки конфигурации.\n"
                + "5 - Открыть папку 'input': Открывает папку 'input' в проводнике.\n"
                + "6 - Открыть папку 'output': Открывает папку 'output' в проводнике.\n"
                + "7 - Очистить папку 'output': Удаляет все файлы из папки 'output'.\n"
                + "8 - Обновить список файлов: Обновляет список JAR-файлов в папке 'input'.\n"
                + "9 - Справка: Отображает это сообщение.\n"
                + "10 - Выход: Закрывает программу.";
        System.out.println(helpMessage);
    }

    private static void printWithDelay(String message) {
        for (char c : message.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
