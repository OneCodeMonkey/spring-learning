package com.liuyang1.springlearning.bootstarter.bootstarter;

public class MyService {
    String name;

    String version;

    public String myFunc() {
        return "In this springboot starter, show name = " + name + ", version = " + version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
