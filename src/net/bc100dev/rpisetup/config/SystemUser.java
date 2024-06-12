package net.bc100dev.rpisetup.config;

public class SystemUser {

    public String name, pass;

    public SystemUser() {
    }

    public SystemUser(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }

    public boolean isRoot() {
        return name.equals("root");
    }

}
