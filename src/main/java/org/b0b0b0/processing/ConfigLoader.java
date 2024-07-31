package org.b0b0b0.processing;
import org.yaml.snakeyaml.Yaml;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
public class ConfigLoader {

    private static final String CONFIG_FILE_NAME = "config.yml";
    private static final File CONFIG_FILE = new File(CONFIG_FILE_NAME);
    private static Map<String, Object> config;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        try {
            if (!CONFIG_FILE.exists()) {
                extractConfigFile();
            }
            try (InputStream inputStream = new FileInputStream(CONFIG_FILE)) {
                Yaml yaml = new Yaml();
                config = yaml.load(inputStream);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    private static void extractConfigFile() {
        try (InputStream inputStream = ConfigLoader.class.getResourceAsStream("/" + CONFIG_FILE_NAME);
             OutputStream outputStream = new FileOutputStream(CONFIG_FILE)) {
            if (inputStream == null) {
                throw new RuntimeException("Configuration file not found in JAR.");
            }
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract configuration file", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static String getFieldName() {
        return ((Map<String, String>) config.get("field")).get("name");
    }

    @SuppressWarnings("unchecked")
    public static String getFieldValue() {
        return ((Map<String, String>) config.get("field")).get("value");
    }

    public static String getComment() {
        return (String) config.get("comment");
    }

    public static String getDetailedComment() {
        return (String) config.get("detailedComment");
    }

    @SuppressWarnings("unchecked")
    public static String getBMFieldName() {
        return ((Map<String, String>) config.get("bmField")).get("name");
    }

    @SuppressWarnings("unchecked")
    public static String getBMFieldValue() {
        return ((Map<String, String>) config.get("bmField")).get("value");
    }
}
