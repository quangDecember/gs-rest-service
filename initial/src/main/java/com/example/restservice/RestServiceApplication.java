package com.example.restservice;

import com.example.restservice.entity.Customer;
import com.example.restservice.repository.CustomerRepository;
import com.example.restservice.web.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RestServiceApplication {

	private static final Logger log = LoggerFactory.getLogger(RestServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RestServiceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	// Accessing Data with JPA  https://spring.io/guides/gs/accessing-data-jpa/
	// Rest service
	// Consuming REST service

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate, CustomerRepository customerRepository) throws Exception {
		return args -> {
			Quote quote = restTemplate.getForObject(
					"https://quoters.apps.pcfone.io/api/random", Quote.class);
			log.info(quote.toString());
			// save a few customers
			customerRepository.save(new Customer("Jack", "Bauer"));
			customerRepository.save(new Customer("Chloe", "O'Brian"));
			customerRepository.save(new Customer("Kim", "Bauer"));
			customerRepository.save(new Customer("David", "Palmer"));
			customerRepository.save(new Customer("Michelle", "Dessler"));

			// fetch all customers
			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			for (Customer customer : customerRepository.findAll()) {
				log.info(customer.toString());
			}
			log.info("");

			// fetch an individual customer by ID
			Customer customer = customerRepository.findById(1L);
			log.info("Customer found with findById(1L):");
			log.info("--------------------------------");
			log.info(customer.toString());
			log.info("");

			// fetch customers by last name
			log.info("Customer found with findByLastName('Bauer'):");
			log.info("--------------------------------------------");
			customerRepository.findByLastName("Bauer").forEach(bauer -> {
				log.info(bauer.toString());
			});
			// for (Customer bauer : repository.findByLastName("Bauer")) {
			//  log.info(bauer.toString());
			// }
			log.info("");
		};
	}
}
