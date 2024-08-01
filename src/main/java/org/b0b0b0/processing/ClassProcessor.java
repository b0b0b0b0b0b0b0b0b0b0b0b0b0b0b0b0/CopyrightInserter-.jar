package org.b0b0b0.processing;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class ClassProcessor {

    private static final String FIELD_NAME = ConfigLoader.getFieldName();
    private static final String FIELD_VALUE = ConfigLoader.getFieldValue();
    private static final int ACCESS = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL;

    public static void processJar(File inputJar, File outputJar) throws IOException {
        File uniqueOutputJar = getUniqueOutputFile(outputJar);

        try (JarFile jar = new JarFile(inputJar);
             JarOutputStream tempJar = new JarOutputStream(new FileOutputStream(uniqueOutputJar))) {

            jar.stream().forEach(entry -> {
                try {
                    if (ConfigLoader.isFieldEnabled() && entry.getName().endsWith(".class")) {
                        ClassReader classReader = new ClassReader(jar.getInputStream(entry));
                        ClassNode classNode = new ClassNode();
                        classReader.accept(classNode, 0);

                        if (classNode.fields.stream().noneMatch(f -> f.name.equals(FIELD_NAME))) {
                            classNode.fields.add(new FieldNode(ACCESS, FIELD_NAME, "Ljava/lang/String;", null, FIELD_VALUE));
                        }

                        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                        classNode.accept(classWriter);
                        byte[] modifiedClass = classWriter.toByteArray();
                        tempJar.putNextEntry(new JarEntry(entry.getName()));
                        tempJar.write(modifiedClass);
                    } else if (!entry.getName().endsWith(".yml") && !entry.getName().endsWith(".json")) {
                        tempJar.putNextEntry(new JarEntry(entry.getName()));
                        tempJar.write(jar.getInputStream(entry).readAllBytes());
                    }
                    tempJar.closeEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            if (ConfigLoader.isCommentEnabled()) {
                YamlProcessor.processYmlFiles(jar, tempJar);
            }

            if (ConfigLoader.isBMFieldEnabled()) {
                JsonModifier.processJsonFiles(jar, tempJar);
            }

            if (ConfigLoader.isDetailedCommentEnabled()) {
                tempJar.setComment(CommentGenerator.generateComment());
            }
        }

        System.out.println("Файл обработан: " + uniqueOutputJar.getName());
    }

    private static File getUniqueOutputFile(File outputJar) {
        String originalName = outputJar.getName();
        String baseName = originalName;
        String extension = "";
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex > 0) {
            baseName = originalName.substring(0, dotIndex);
            extension = originalName.substring(dotIndex);
        }

        int counter = 1;
        File uniqueFile = new File(outputJar.getParent(), counter + "_" + originalName);
        while (uniqueFile.exists()) {
            counter++;
            uniqueFile = new File(outputJar.getParent(), counter + "_" + baseName + extension);
        }
        return uniqueFile;
    }
}
