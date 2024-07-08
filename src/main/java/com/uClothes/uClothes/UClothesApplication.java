package com.uClothes.uClothes;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class UClothesApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String dbUsername = dotenv.get("DB_USERNAME");
        String dbPassword = dotenv.get("DB_PASSWORD");
        String dbUrl = dotenv.get("DB_URL");

        if (dbUsername == null || dbPassword == null || dbUrl == null) {
            System.err.println("One or more required environment variables are missing. Please check your .env file.");
            System.exit(1);
        }

        System.setProperty("DB_USERNAME", dbUsername);
        System.setProperty("DB_PASSWORD", dbPassword);
        System.setProperty("DB_URL", dbUrl);
        SpringApplication.run(UClothesApplication.class, args);
    }

}
