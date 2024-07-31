package org.b0b0b0.processing;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class YamlProcessor {

    private static final String COPYRIGHT_COMMENT = ConfigLoader.getComment();

    public static void processYmlFiles(JarFile jarFile, JarOutputStream tempJar) throws IOException {
        jarFile.stream().forEach(entry -> {
            try {
                if (entry.getName().endsWith(".yml")) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(entry), StandardCharsets.UTF_8));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos, StandardCharsets.UTF_8));
                    writer.write(COPYRIGHT_COMMENT);
                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.newLine();
                    }
                    writer.flush();
                    reader.close();
                    writer.close();
                    tempJar.putNextEntry(new JarEntry(entry.getName()));
                    tempJar.write(baos.toByteArray());
                    tempJar.closeEntry();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
