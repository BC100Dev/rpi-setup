package net.bc100dev.rpisetup.config;

import java.util.ArrayList;
import java.util.List;

public class AppProcess {

    public String language = "en_US",
            hostname = "raspberrypi",
            defaultUsername = "rpi";

    public List<SystemUser> systemUsers = new ArrayList<>(),
            regularUsers = new ArrayList<>();

    public SystemUser rootUser = new SystemUser("root", "");

    public Swap swap = new Swap(false, 1024L * 1024L * 1024L);

    public Network network;

    public List<String> optAptRepos = new ArrayList<>(),
            additionalAptPkgs = new ArrayList<>();

}
