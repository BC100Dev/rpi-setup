package net.bc100dev.rpisetup;

import net.bc100dev.commons.utils.OperatingSystem;
import net.bc100dev.commons.utils.Utility;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static net.bc100dev.commons.utils.RuntimeEnvironment.getOperatingSystem;

public class RPIMain {

    private static void display() {
        ProcessHandle handle = ProcessHandle.current();
        ProcessHandle.Info info = handle.info();
        String cmd = info.command().isPresent() ? "./" + info.command().get() : "./rpi-setup";

        if (cmd.equalsIgnoreCase("./java"))
            cmd = "./rpi-setup.jar";

        System.out.println("RPI Setup v1");
        System.out.println("usage:");
        System.out.println(cmd + " -li config.json");
        System.out.println("    loads in the 'config.json' file");
        System.out.println(cmd + " -el config.enc");
        System.out.println("    loads in an encrypted 'config.json' file");
        System.out.println(cmd + " -ew config.json encrypted-config.enc");
        System.out.println("    encrypts your current config.json file into a new, 'encrypted-config.enc' file");
        System.out.println();
        System.out.println("View https://github.com/BeChris100/osintgram4j/tree/master/extres/examples to view its usage " +
                "information on configuration files");
    }

    private static void start(File configFile) {
        StringBuilder strBD = new StringBuilder();
        FileInputStream configFis = null;

        try {
            configFis = new FileInputStream(configFile);
            byte[] buff = new byte[4096];
            int len;

            while ((len = configFis.read(buff, 0, 4096)) != -1)
                strBD.append(new String(buff, 0, len));
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        } finally {
            if (configFis != null) {
                try {
                    configFis.close();
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                }
            }
        }

        String jsonData = strBD.toString().trim();
        if (!(jsonData.startsWith("{") && jsonData.endsWith("}"))) {
            System.err.println("Invalid configuration file format");
            System.exit(1);
            return;
        }

        JSONObject root = new JSONObject(jsonData);
    }

    public static void main(String[] args) {
        if (getOperatingSystem() != OperatingSystem.LINUX) {
            System.err.println("Unable to start rpi-setup");
            System.err.println("This tool only supports Linux");
            System.exit(1);
        }

        if (NativeLoader.hasLibrary())
            NativeLoader.load();
        else {
            System.err.println("Unable to load native library: \"librpi-setup.so\"");
            System.err.println("Library not found");
            System.exit(1);
            return;
        }

        if (args == null || args.length == 0) {
            display();
            System.exit(0);
            return;
        }

        try {
            File blkidBin = Utility.getBinaryPath("blkid");
            if (blkidBin == null)
                throw new IOException("blkid path not initialized");

            ProcessBuilder pb = new ProcessBuilder(blkidBin.getAbsolutePath());
            pb.start();
        } catch (IOException ex) {
            String msg = ex.getMessage().toLowerCase();
            if ((msg.contains("error=2") && msg.contains("no such file or directory")) ||
                    ex.getMessage().equals("blkid path not initialized")) {
                System.err.println("\"blkid\" was not found on your system.");
                System.err.println("Make sure to install the \"util-linux\" package.");
            } else {
                System.err.println("An unknown error has just occurred.");
                ex.printStackTrace(System.err);
            }

            System.exit(1);
            return;
        }

        File configFile = new File(args[0]);
        if (!configFile.exists()) {
            System.err.println("No config file found.");
            System.exit(1);
            return;
        }

        if (!configFile.canRead()) {
            System.err.printf("\"%s\" is not readable.\n", configFile.getPath());
            System.exit(1);
            return;
        }

        start(configFile);
    }

}
