package com.myjourneyblog.MyJourneyBlog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class MyJourneyBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyJourneyBlogApplication.class, args);
	}

	@Bean
	CommandLineRunner testDatabaseConnection(DataSource dataSource) {
		return args -> {
			try (Connection connection = dataSource.getConnection()) {
				System.out.println("✅Database connection successful!");
				System.out.println("Database: " + connection.getCatalog());
				System.out.println("URL: " + connection.getMetaData().getURL());
			} catch (Exception e) {
				System.out.println("❌Database connection failed!");
				e.printStackTrace();
			}
		};
	}

}
