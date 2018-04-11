package com.ailikes.util;

import java.io.FileNotFoundException;

import xorg.springframework.util.LogbackConfigurer;

public class LogBackConfigurer {
    private String location;

    public void init() throws FileNotFoundException {
        LogbackConfigurer.initLogging(location);
    }

    public void destory() {
        LogbackConfigurer.shutdownLogging();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
