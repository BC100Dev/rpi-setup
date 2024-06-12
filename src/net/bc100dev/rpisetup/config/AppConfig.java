package net.bc100dev.rpisetup.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AppConfig {

    public int configVersion = 1;
    public File imageFile;
    public MountPath rootfs = new MountPath("rootfs", new File("/mnt/rootfs"));
    public MountPath bootfs = new MountPath("bootfs", new File("/mnt/bootfs"));
    public SystemKernel kernel = new SystemKernel(false, new File("/dev/null"), "https://github.com/raspberrypi/linux", "rpi-5.15.y");
    public AppProcess process;
    public List<ConfigAction> customActions = new ArrayList<>();

}
