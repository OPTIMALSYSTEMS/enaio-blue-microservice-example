package com.os.enaio.test;

import com.os.services.blue.connector.BlueConnector;
import com.os.services.blue.error.EnableErrorHandling;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by cschulze on 15.03.2017.
 */
@SpringBootApplication
@BlueConnector
@EnableErrorHandling
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
