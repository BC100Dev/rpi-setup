package net.bc100dev.rpisetup.config;

import java.io.File;

public class SystemKernel {

    /*
    "UsePrebuilt": false,
        "PrebuiltKernelImageFile": "path/to/prebuilt/kernel.img",
     */

    public boolean prebuilt = false;
    public File prebuiltKernelImage;

    public String remoteURL = "https://github.com/raspberrypi/linux";
    public String remoteBranch = "rpi-5.15.y";

    public SystemKernel() {
    }

    public SystemKernel(boolean prebuilt, File prebuiltKernelImage, String remoteURL, String remoteBranch) {
        this.prebuilt = prebuilt;
        this.prebuiltKernelImage = prebuiltKernelImage;
        this.remoteURL = remoteURL;
        this.remoteBranch = remoteBranch;
    }

}
