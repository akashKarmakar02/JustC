package com.amberj.lib;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ProjectDataLib {
    private static final String DATA_FILE_NAME = "project_data.properties";
    private final Path dataFilePath;

    public enum ProjectDataKey {
        PROJECT_DIRECTORY("project.directory"),
        OPENED_FILES("opened.files"),
        VIEW_TAB_FILES("view.tab.files");

        private final String key;

        ProjectDataKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public ProjectDataLib() throws IOException {
        dataFilePath = getDataFilePath();
        createDataFileIfNotExists();
    }

    // Get the path for the data file in the user's home directory
    private Path getDataFilePath() {
        return Paths.get(System.getProperty("user.home"), DATA_FILE_NAME);
    }

    private void createDataFileIfNotExists() throws IOException {
        if (!Files.exists(dataFilePath)) {
            Files.createFile(dataFilePath);
        }
    }

    public void setProperty(ProjectDataKey key, String value) {
        setData(key.getKey(), value);
    }

    public String getProperty(ProjectDataKey key) {
        return getData(key.getKey());
    }

    public void setPropertyList(ProjectDataKey key, List<String> values) {
        setDataList(key.getKey(), values);
    }

    public List<String> getPropertyList(ProjectDataKey key) {
        return getDataList(key.getKey());
    }

    private void setData(String key, String value) {
        try (InputStream input = new FileInputStream(dataFilePath.toFile())) {
            Properties properties = new Properties();
            properties.load(input);
            properties.setProperty(key, value);

            try (OutputStream output = new FileOutputStream(dataFilePath.toFile())) {
                properties.store(output, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getData(String key) {
        try (InputStream input = new FileInputStream(dataFilePath.toFile())) {
            Properties properties = new Properties();
            properties.load(input);
            return properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setDataList(String key, List<String> values) {
        String joinedValues = String.join(",", values);
        setData(key, joinedValues);
    }

    private List<String> getDataList(String key) {
        String data = getData(key);
        List<String> list = new ArrayList<>();
        if (data != null && !data.isEmpty()) {
            for (String value : data.split(",")) {
                list.add(value.trim());
            }
        }
        return list;
    }
}
