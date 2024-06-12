package net.bc100dev.rpisetup.config;

import java.io.File;

public class MountPath {

    public String label;
    public File path;

    public MountPath() {
    }

    public MountPath(String label, File path) {
        this.label = label;
        this.path = path;
    }

}
