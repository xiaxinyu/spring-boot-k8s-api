package com.hds.k8s;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SprintBootK8sApplication {
	private static final Logger log = LoggerFactory.getLogger(SprintBootK8sApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SprintBootK8sApplication.class, args);
	}

	@GetMapping("/say/hello")
	public String sayHello(String name) {
		log.info("Invoking the method /say/hello");
		return "Hello," + name;
	}
}
