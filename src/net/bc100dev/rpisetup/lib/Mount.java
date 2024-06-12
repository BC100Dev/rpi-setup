package net.bc100dev.rpisetup.lib;

import net.bc100dev.commons.utils.Utility;
import rpisetup.commons.Library;
import rpisetup.commons.LibraryExecOutput;

import java.io.File;
import java.io.IOException;

public class Mount extends Library {

    private File mountBin;

    private String options = "errors=remount-ro";

    private native void nMount(String src, String target, String opts) throws IOException;
    private native void nUnmount(String src) throws IOException;

    @Override
    public LibraryExecOutput libraryEntry(String[] args) {
        try {
            mountBin = Utility.getBinaryPath("mount");
            if (mountBin == null)
                throw new IOException("Could not find \"mount\"");
        } catch (IOException ex) {
            return new LibraryExecOutput(1, Utility.throwableToString(ex));
        }

        return new LibraryExecOutput(0, "");
    }
}
