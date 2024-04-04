package com.example.kafkabugdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;

@SpringBootApplication
public class KafkaBugDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaBugDemoApplication.class, args);
    }

    @Bean
    public Consumer<String> consumer() {
        return data -> System.out.println("Data: " + data);
    }

}
