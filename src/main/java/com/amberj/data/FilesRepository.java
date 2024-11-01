package com.amberj.data;

import com.amberj.lib.ProjectDataLib;

import java.util.List;

public class FilesRepository {

    private final ProjectDataLib projectDataLib;

    public FilesRepository(ProjectDataLib projectDataLib) {
        this.projectDataLib = projectDataLib;
    }

    public List<String> getLastOpenedFiles() {
        return projectDataLib.getPropertyList(ProjectDataLib.ProjectDataKey.OPENED_FILES);
    }

    public String getLastViewFile() {
        return projectDataLib.getProperty(ProjectDataLib.ProjectDataKey.VIEW_TAB_FILES);
    }

    public void saveLastOpenedFiles(List<String> openedFiles) {
        projectDataLib.setPropertyList(ProjectDataLib.ProjectDataKey.OPENED_FILES, openedFiles);
    }

    public void saveLastViewedFile(String filePath) {
        projectDataLib.setProperty(ProjectDataLib.ProjectDataKey.VIEW_TAB_FILES, filePath);
    }
}
