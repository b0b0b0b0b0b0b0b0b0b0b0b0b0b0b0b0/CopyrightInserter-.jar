package org.b0b0b0.processing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class JsonModifier {

    private static final String BM_FIELD = ConfigLoader.getBMFieldName();
    private static final String BM_VALUE = ConfigLoader.getBMFieldValue();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void processJsonFiles(JarFile jarFile, JarOutputStream tempJar) throws IOException {
        jarFile.stream().forEach(entry -> {
            try {
                if (entry.getName().endsWith(".json")) {
                    JsonNode rootNode = OBJECT_MAPPER.readTree(jarFile.getInputStream(entry));
                    if (rootNode.isObject()) {
                        ObjectNode objectNode = (ObjectNode) rootNode;
                        objectNode.put(BM_FIELD, BM_VALUE);
                    }
                    byte[] modifiedJson = OBJECT_MAPPER.writeValueAsBytes(rootNode);
                    tempJar.putNextEntry(new JarEntry(entry.getName()));
                    tempJar.write(modifiedJson);
                    tempJar.closeEntry();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
