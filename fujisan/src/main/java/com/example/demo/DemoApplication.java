package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@SpringBootApplication
@RestController
public class DemoApplication {

	private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);
	private final Random random = new Random();

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/api/process")
	public String processRequest(@RequestParam(defaultValue = "500") int maxDelay) throws InterruptedException {
		// Simulate a variable processing time between 50ms and maxDelay
		int delay = random.nextInt(maxDelay) + 50;
		log.info("Processing request. Simulated delay: {}ms", delay);
		
		Thread.sleep(delay); // Simulate work

		// Give a 10% chance to simulate a failure to generate error rates in Grafana
		if (random.nextInt(10) == 0) {
			log.error("A random simulated error occurred processing the request!");
			throw new RuntimeException("Simulated Internal Server Error for Observability Demo");
		}

		return "Process completed successfully in " + delay + "ms!";
	}

	@GetMapping("/api/fast")
	public String fastRequest() {
		log.info("Processing fast request");
		return "Completed instantly!";
	}

}
