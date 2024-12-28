package com.cs2404.tablebuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class TableBuddyApplication {

    public static void main(String[] args) {
        SpringApplication.run(TableBuddyApplication.class, args);
    }

}
