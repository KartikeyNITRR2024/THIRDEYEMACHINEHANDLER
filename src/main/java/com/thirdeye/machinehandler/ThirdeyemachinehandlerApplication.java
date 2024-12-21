package com.thirdeye.machinehandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ThirdeyemachinehandlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThirdeyemachinehandlerApplication.class, args);
	}

}
