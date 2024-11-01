package com.amberj.lib;

public class ProjectManager {

    private final ProjectDataLib projectDataLib;

    public ProjectManager(ProjectDataLib projectDataLib) {
        this.projectDataLib = projectDataLib;
    }

    public void openProject(String path) {
        projectDataLib.setProperty(ProjectDataLib.ProjectDataKey.PROJECT_DIRECTORY, path);


    }

}
