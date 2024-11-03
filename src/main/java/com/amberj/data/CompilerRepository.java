package com.amberj.data;

import com.amberj.lib.ProjectDataLib;
import org.jetbrains.annotations.NotNull;

public class CompilerRepository {

    private final ProjectDataLib projectDataLib;


    public CompilerRepository(ProjectDataLib projectDataLib) {
        this.projectDataLib = projectDataLib;
    }

    public void saveCompilerPath(@NotNull String path) {
        projectDataLib.setProperty(ProjectDataLib.ProjectDataKey.GCC_PATH, path);
    }

    public String getCompilerPath() {
        return projectDataLib.getProperty(ProjectDataLib.ProjectDataKey.GCC_PATH);
    }
}
