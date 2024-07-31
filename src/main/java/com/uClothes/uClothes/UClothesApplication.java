package com.uClothes.uClothes;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;


@SpringBootApplication
public class UClothesApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String dbUsername = dotenv.get("DB_USERNAME");
        String dbPassword = dotenv.get("DB_PASSWORD");
        String dbUrl = dotenv.get("DB_URL");
        String jwtSecret = dotenv.get("JWT_SECRET");


        if (dbUsername == null || dbPassword == null || dbUrl == null || jwtSecret == null) {
            System.err.println("One or more required environment variables are missing. Please check your .env file.");
            System.exit(1);
        }
        System.setProperty("DB_USERNAME", dbUsername);
        System.setProperty("DB_PASSWORD", dbPassword);
        System.setProperty("DB_URL", dbUrl);
        System.setProperty("JWT_SECRET", jwtSecret);

        File uploadDir = new File("uploads");
        if (!uploadDir.exists()) {
            if (!uploadDir.mkdirs()) {
                System.err.println("Failed to create upload directory.");
                System.exit(1);
            }
        }
        SpringApplication.run(UClothesApplication.class, args);
    }

}
