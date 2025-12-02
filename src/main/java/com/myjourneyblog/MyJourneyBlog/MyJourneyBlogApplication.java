package com.myjourneyblog.MyJourneyBlog;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.TimeZone;

@SpringBootApplication
public class MyJourneyBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyJourneyBlogApplication.class, args);
	}

	/**
	 * Set the application's default TimeZone to user local time
	 */
	@PostConstruct
	public void init() {
		// Change "Asia/Tokyo" to desired timezone if different
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"));
		System.out.println("✅ Application TimeZone set to: " + TimeZone.getDefault().getID());
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
