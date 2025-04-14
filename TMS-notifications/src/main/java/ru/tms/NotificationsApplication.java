package ru.tms;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class NotificationsApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationsApplication.class, args);
    }
}