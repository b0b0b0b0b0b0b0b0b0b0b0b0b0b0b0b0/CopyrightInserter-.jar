package org.b0b0b0.processing;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DirectoryManager {

    public static File createDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists() && !directory.mkdirs()) {
            System.out.println("Не удалось создать папку: " + directoryPath);
        }
        return directory;
    }

    public static List<File> getJarFiles(File directory) {
        List<File> jarFiles = new ArrayList<>();
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".jar"));
            if (files != null) {
                jarFiles.addAll(Arrays.asList(files));
            }
        }
        return jarFiles;
    }
}
