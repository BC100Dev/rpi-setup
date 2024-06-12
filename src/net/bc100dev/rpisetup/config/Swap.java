package net.bc100dev.rpisetup.config;

public class Swap {

    public boolean enabled;
    public long size;

    public Swap() {
    }

    public Swap(boolean enabled, long size) {
        this.enabled = enabled;
        this.size = size;
    }

}
