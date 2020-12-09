package com.readproperties;

import java.io.FileInputStream;
import java.util.Properties;

public class Application {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.load(new FileInputStream("src/main/resources/config.properties"));

        System.out.println(props.getProperty("username"));
        System.out.println(props.getProperty("password"));
    }
}