package com.ka5ta.drivers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.SpringVersion;

@SpringBootApplication
public class DriversApplication {

	public static void main(String[] args) {

		SpringApplication.run(DriversApplication.class, args);
		/* DatabaseConnection db = new DatabaseConnection();
		db.connect(); */


	}

}
