package net.bc100dev.rpisetup.lib;

import net.bc100dev.commons.utils.Utility;
import org.json.JSONArray;
import org.json.JSONObject;
import rpisetup.commons.ExecutionOutput;
import rpisetup.commons.Library;
import rpisetup.commons.LibraryExecOutput;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Losetup extends Library {

    private String image;
    private Action action;

    private File cmdBin;


    private ExecutionOutput runCommand(String... args) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(cmdBin.getAbsolutePath());
        pb.command(args);
        pb.redirectErrorStream(false);

        Process pc = pb.start();
        int code = pc.waitFor();

        DataInputStream dis = new DataInputStream(pc.getInputStream());
        DataInputStream des = new DataInputStream(pc.getErrorStream());

        byte[] bi = dis.readAllBytes();
        byte[] be = des.readAllBytes();

        return new ExecutionOutput(code, bi, be);
    }

    private File[] getPartitions(String blockDevice) throws IOException, InterruptedException {
        List<File> parts = new ArrayList<>();

        File devDir = new File("/dev");
        File[] contents = devDir.listFiles();
        if (contents == null)
            return new File[0];

        for (File f : contents) {
            if (f.getName().equals(blockDevice))
                continue;

            if (f.getName().startsWith(blockDevice + "p"))
                parts.add(f);
        }

        Collections.sort(parts);
        return parts.toArray(new File[0]);
    }

    private String[] devBlock() throws IOException, InterruptedException {
        ExecutionOutput output = runCommand("-J");

        if (output.rc() != 0)
            throw new RuntimeException(String.format("Error running command \"losetup\" (returned %d): %s | %s", output.rc(),
                    new String(output.errorStreamData()), new String(output.outputStreamData())));

        String jsonData = new String(output.errorStreamData()).trim();
        if (!(jsonData.startsWith("{") || jsonData.startsWith("}")))
            throw new RuntimeException("Invalid losetup json data");

        JSONObject root = new JSONObject(jsonData);
        JSONArray loopDevices = root.getJSONArray("loopdevices");

        for (int i = 0; i < loopDevices.length(); i++) {
            JSONObject device = loopDevices.getJSONObject(i);
            if (!device.has("back-file"))
                continue;

            File imageFile = new File(image);
            File pointerFile = new File(device.getString("back-file"));

            if (imageFile.getAbsolutePath().equals(pointerFile.getAbsolutePath()))
                return new String[]{device.getString("name"), pointerFile.getAbsolutePath()};
        }

        return null;
    }

    private LibraryExecOutput runAction() throws IOException, InterruptedException {
        String templateErrorMsg = """
                Error occurred: %d
                
                ErrorStream:
                %s
                
                OutputStream:
                %s""";

        switch (action) {
            case ATTACH -> {
                // verify, if the block device even exists
                String[] block = devBlock();
                if (block == null) {
                    // create block device
                    ExecutionOutput exec = runCommand("-f", "-P", "--show", image);
                    if (exec.rc() != 0)
                        return new LibraryExecOutput(exec.rc(), templateErrorMsg.formatted(exec.rc(),
                                new String(exec.errorStreamData()), new String(exec.outputStreamData())));

                    return new LibraryExecOutput(0, new String(exec.outputStreamData()));
                } else
                    System.out.println();
            }
            case DETACH -> {
                String[] block = devBlock();
                if (block == null)
                    // not attached
                    return new LibraryExecOutput(0, "");

                ExecutionOutput exec = runCommand("-d", block[0]);
                if (exec.rc() != 0)
                    return new LibraryExecOutput(exec.rc(), templateErrorMsg.formatted(exec.rc(),
                            new String(exec.errorStreamData()), new String(exec.outputStreamData())));

                return new LibraryExecOutput(0, "");
            }
        }

        return new LibraryExecOutput(1, "unknown action");
    }

    @Override
    public LibraryExecOutput libraryEntry(String[] args) {
        try {
            cmdBin = Utility.getBinaryPath("losetup");
            if (cmdBin == null)
                throw new IOException("Path for binary \"losetup\" not found");
        } catch (IOException ex) {
            return new LibraryExecOutput(1, Utility.throwableToString(ex));
        }

        if (args.length < 2) {
            String msg = """
                    Library "losetup"
                    requires two arguments:
                        [image]     name of the image file
                        [action]    action to pass for losetup command (passes either "attach" or "detach")""";

            return new LibraryExecOutput(1, msg);
        }

        String _image = args[0];
        String _action = args[1];

        switch (_action.toLowerCase()) {
            case "attach" -> action = Action.ATTACH;
            case "detach" -> action = Action.DETACH;
            default -> System.err.println("unknown action \"" + _action + "\"");
        }

        this.image = _image;

        try {
            return runAction();
        } catch (IOException | InterruptedException ex) {
            return new LibraryExecOutput(1, Utility.throwableToString(ex));
        }
    }

    private enum Action {

        ATTACH,

        DETACH

    }

}
