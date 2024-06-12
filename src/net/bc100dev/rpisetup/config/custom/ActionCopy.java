package net.bc100dev.rpisetup.config.custom;

import net.bc100dev.rpisetup.config.ConfigAction;

public class ActionCopy extends ConfigAction {

    private native void chmod(String sourceFile, String mode);
    private native void chown(String sourceFile, String owner, String group);

    @Override
    public void executeAction() {
    }

    @Override
    public boolean canExecuteAction() {
        return executionMap.get("source") == null || executionMap.get("dest") == null;
    }
}
