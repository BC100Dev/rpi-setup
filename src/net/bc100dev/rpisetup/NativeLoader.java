package net.bc100dev.rpisetup;

import java.io.File;

public class NativeLoader {

    private static boolean loaded = false;

    public static boolean hasLibrary() {
        String[] paths = System.getProperty("java.library.path").split(File.pathSeparator);
        for (String path : paths) {
            File libFile = new File(path, System.mapLibraryName("rpi-setup"));
            if (libFile.exists())
                return true;
        }

        return false;
    }

    public static boolean isLoaded() {
        return loaded;
    }

    public static void load() {
        System.loadLibrary("rpi-setup");
        loaded = true;
    }

}
