package org.b0b0b0.processing;

import org.snakeyaml.engine.v2.api.Dump;
import org.snakeyaml.engine.v2.api.DumpSettings;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;

public class ConfigLoader {

    private static final String CONFIG_FILE_NAME = "config.yml";
    private static File configFile = new File(CONFIG_FILE_NAME);
    private static Map<String, Object> config;
    private static boolean isConfigNewlyCreated = false;

    static {
        isConfigNewlyCreated = ensureConfigFileExists();
        loadConfig();
    }

    public static boolean ensureConfigFileExists() {
        if (!configFile.exists()) {
            try (InputStream resourceStream = ConfigLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)) {
                if (resourceStream == null) {
                    throw new RuntimeException("Ресурсный файл конфигурации не найден в JAR: " + CONFIG_FILE_NAME);
                }
                Files.copy(resourceStream, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                return true;
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при создании конфигурационного файла", e);
            }
        }
        return false;
    }

    private static void loadConfig() {
        try (InputStream inputStream = new FileInputStream(configFile)) {
            LoadSettings settings = LoadSettings.builder().build();
            Load load = new Load(settings);
            config = (Map<String, Object>) load.loadFromInputStream(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке конфигурационного файла", e);
        }
    }

    public static boolean isConfigNewlyCreated() {
        return isConfigNewlyCreated;
    }

    public static void reloadConfig() {
        loadConfig();
    }

    @SuppressWarnings("unchecked")
    public static String getFieldName() {
        return ((Map<String, String>) config.get("field")).get("name");
    }

    @SuppressWarnings("unchecked")
    public static String getFieldValue() {
        return ((Map<String, String>) config.get("field")).get("value");
    }

    public static boolean isFieldEnabled() {
        return (boolean) ((Map<String, Object>) config.get("field")).get("enabled");
    }

    public static boolean isCommentEnabled() {
        return (boolean) config.get("commentEnabled");
    }

    public static String getComment() {
        return (String) config.get("comment");
    }

    public static boolean isDetailedCommentEnabled() {
        return (boolean) config.get("detailedCommentEnabled");
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

    public static boolean isBMFieldEnabled() {
        return (boolean) ((Map<String, Object>) config.get("bmField")).get("enabled");
    }

    public static boolean isAnnotationsEnabled() {
        return (boolean) ((Map<String, Object>) config.get("annotations")).get("enabled");
    }

    @SuppressWarnings("unchecked")
    public static String getClassAnnotationName() {
        return (String) ((Map<String, String>) ((Map<String, Object>) config.get("annotations")).get("classAnnotation")).get("name");
    }

    @SuppressWarnings("unchecked")
    public static String getClassAnnotationValue() {
        return (String) ((Map<String, String>) ((Map<String, Object>) config.get("annotations")).get("classAnnotation")).get("value");
    }

    @SuppressWarnings("unchecked")
    public static String getFieldAnnotationName() {
        return (String) ((Map<String, String>) ((Map<String, Object>) config.get("annotations")).get("fieldAnnotation")).get("name");
    }

    @SuppressWarnings("unchecked")
    public static String getFieldAnnotationValue() {
        return (String) ((Map<String, String>) ((Map<String, Object>) config.get("annotations")).get("fieldAnnotation")).get("value");
    }


    public static void setLanguage(String language) {
        config.put("language", language);
        saveConfig();
    }

    private static void saveConfig() {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8)) {
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(true);
            Yaml yaml = new Yaml(options);
            yaml.dump(config, writer);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении конфигурационного файла", e);
        }
    }

    public static String getLanguage() {
        return (String) config.getOrDefault("language", "ru");
    }
    @SuppressWarnings("unchecked")
    public static String getMessage(String key) {
        String language = getLanguage();
        Map<String, Object> messages = (Map<String, Object>) config.get("messages");
        Map<String, String> languageMessages = (Map<String, String>) messages.get(language);
        return languageMessages.getOrDefault(key, "Сообщение не найдено: " + key);
    }
}
