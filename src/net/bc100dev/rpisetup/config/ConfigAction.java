package net.bc100dev.rpisetup.config;

import java.util.HashMap;
import java.util.Map;

public abstract class ConfigAction {

    public Map<String, Object> executionMap = new HashMap<>();

    public abstract boolean canExecuteAction();

    public abstract void executeAction();

}
