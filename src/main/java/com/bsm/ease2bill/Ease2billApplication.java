package com.bsm.ease2bill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
})

public class Ease2billApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ease2billApplication.class, args);
	}

}
