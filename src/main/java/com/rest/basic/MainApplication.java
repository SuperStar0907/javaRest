package com.rest.basic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {

//	Best way to debug - try and catch
//	public static void main(String[] args) {
//		try {
//			SpringApplication.run(MainApplication.class, args);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public static void main(String[] args) {

			SpringApplication.run(MainApplication.class, args);
	}
}
