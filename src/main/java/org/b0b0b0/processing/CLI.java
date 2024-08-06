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
            System.out.println(ConfigLoader.getMessage("config_created"));
            System.out.println(ConfigLoader.getMessage("config_not_required"));
            System.out.println(" ");
        }

        System.out.println(ConfigLoader.getMessage("start_processing"));
        autoProcessJars();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayMenu();
            System.out.print("> ");
            String command = scanner.nextLine().trim();

            switch (command) {
                case "1":
                    System.out.println(" ");
                    System.out.println(ConfigLoader.getMessage("processing_jars"));
                    autoProcessJars();
                    break;

                case "2":
                    System.out.println(" ");
                    System.out.println(ConfigLoader.getMessage("reload_config"));
                    ConfigLoader.reloadConfig();
                    System.out.println(ConfigLoader.getMessage("config_reloaded"));
                    System.out.println(" ");
                    break;

                case "3":
                    System.out.println(ConfigLoader.getMessage("list_jars"));
                    listAvailableJars();
                    break;

                case "4":
                    System.out.println(ConfigLoader.getMessage("show_config"));
                    showConfigFile();
                    break;

                case "5":
                    System.out.println(ConfigLoader.getMessage("open_input_dir"));
                    openDirectory("input");
                    break;

                case "6":
                    System.out.println(ConfigLoader.getMessage("open_output_dir"));
                    openDirectory("output");
                    break;

                case "7":
                    System.out.println(ConfigLoader.getMessage("clear_output_dir"));
                    clearOutputDirectory();
                    break;

                case "8":
                    System.out.println(ConfigLoader.getMessage("update_file_list"));
                    autoProcessJars();
                    break;

                case "9":
                    System.out.println(ConfigLoader.getMessage("show_help"));
                    displayHelp();
                    break;

                case "10":
                    System.out.println(ConfigLoader.getMessage("exit_program"));
                    scanner.close();
                    return;

                case "11":
                    toggleLanguage();
                    break;

                default:
                    System.out.println(ConfigLoader.getMessage("unknown_command"));
                    break;
            }
        }
    }

    private static void displayMenu() {
        System.out.println(" ");
        System.out.println(ConfigLoader.getMessage("menu_prompt"));
        System.out.println(ConfigLoader.getMessage("menu_option_1"));
        System.out.println(ConfigLoader.getMessage("menu_option_2"));
        System.out.println(ConfigLoader.getMessage("menu_option_3"));
        System.out.println(ConfigLoader.getMessage("menu_option_4"));
        System.out.println(ConfigLoader.getMessage("menu_option_5"));
        System.out.println(ConfigLoader.getMessage("menu_option_6"));
        System.out.println(ConfigLoader.getMessage("menu_option_7"));
        System.out.println(ConfigLoader.getMessage("menu_option_8"));
        System.out.println(ConfigLoader.getMessage("menu_option_9"));
        System.out.println(ConfigLoader.getMessage("menu_option_10"));
        System.out.println("11 - " + ConfigLoader.getMessage("toggle_language"));
        System.out.println(" ");
    }

    private static void autoProcessJars() {
        File inputDirectory = DirectoryManager.createDirectory("input");
        List<File> jarFiles = DirectoryManager.getJarFiles(inputDirectory);

        if (jarFiles.isEmpty()) {
            String message = ConfigLoader.getMessage("input_folder_empty") + "\n"
                    + ConfigLoader.getMessage("input_folder_instructions") + "\n"
                    + ConfigLoader.getMessage("input_folder_prompt") + "\n"
                    + ConfigLoader.getMessage("input_folder_command_instructions") + "\n"
                    + ConfigLoader.getMessage("reload_config_prompt") + "\n"
                    + ConfigLoader.getMessage("exit_instruction") + "\n";
            printWithDelay(message);
            return;
        }

        for (File inputJar : jarFiles) {
            File outputJar = new File("output", inputJar.getName());
            outputJar.getParentFile().mkdirs();
            try {
                ClassProcessor.processJar(inputJar, outputJar);
                System.out.println(ConfigLoader.getMessage("donor_file") + inputJar.getName());
            } catch (IOException e) {
                System.err.println(ConfigLoader.getMessage("processing_error") + inputJar.getName());
                e.printStackTrace();
            }
        }
    }

    private static void listAvailableJars() {
        File inputDirectory = new File("input");
        List<File> jarFiles = DirectoryManager.getJarFiles(inputDirectory);

        if (jarFiles.isEmpty()) {
            System.out.println(ConfigLoader.getMessage("no_jar_files"));
            return;
        }

        System.out.println(ConfigLoader.getMessage("available_jar_files"));
        for (File jarFile : jarFiles) {
            System.out.println("- " + jarFile.getName());
        }
    }

    private static void showConfigFile() {
        try {
            List<String> lines = Files.readAllLines(Path.of("config.yml"));
            System.out.println(ConfigLoader.getMessage("config_content"));
            lines.forEach(System.out::println);
        } catch (IOException e) {
            System.err.println(ConfigLoader.getMessage("config_read_error"));
        }
    }

    private static void openDirectory(String dirName) {
        File directory = new File(dirName);
        if (directory.exists() && directory.isDirectory()) {
            try {
                Desktop.getDesktop().open(directory);
                System.out.println(String.format(ConfigLoader.getMessage("directory_opened"), dirName));
            } catch (IOException e) {
                System.err.println(String.format(ConfigLoader.getMessage("directory_open_error"), dirName));
            }
        } else {
            System.out.println(String.format(ConfigLoader.getMessage("directory_not_found"), dirName));
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
                            System.out.println(ConfigLoader.getMessage("file_deleted") + file.getName());
                        } else {
                            System.out.println(ConfigLoader.getMessage("file_delete_failed") + file.getName());
                        }
                    }
                }
            }
        } else {
            System.out.println(ConfigLoader.getMessage("output_folder_not_found"));
        }
    }

    private static void displayHelp() {
        String helpMessage = ConfigLoader.getMessage("help_message");
        System.out.println(helpMessage);
    }

    private static void toggleLanguage() {
        String currentLanguage = ConfigLoader.getLanguage();
        String newLanguage = currentLanguage.equals("ru") ? "en" : "ru";
        ConfigLoader.setLanguage(newLanguage);
        System.out.println(ConfigLoader.getMessage("language_changed"));
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
