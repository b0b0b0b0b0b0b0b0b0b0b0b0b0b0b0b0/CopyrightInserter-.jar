package org.b0b0b0.processing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryManager {

    public static File createDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Directory created: " + directoryPath);
            } else {
                System.out.println("Failed to create directory: " + directoryPath);
            }
        }
        return directory;
    }

    public static List<File> getJarFiles(File directory) {
        List<File> jarFiles = new ArrayList<>();
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".jar"));
            if (files != null) {
                for (File file : files) {
                    jarFiles.add(file);
                }
            }
        }
        return jarFiles;
    }
}
