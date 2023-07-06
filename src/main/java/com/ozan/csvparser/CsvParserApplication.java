package com.ozan.csvparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.ozan.csvparser.entity")
@EnableJpaRepositories("com.ozan.csvparser.repository")
public class CsvParserApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(CsvParserApplication.class, args);
	}

}
