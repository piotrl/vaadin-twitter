package net.piotrl;

import net.piotrl.analyser.scrapper.Tweet;
import net.piotrl.imports.Customer;
import net.piotrl.imports.TweetsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class Application {
	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner loadData(TweetsRepository tweetsRepository) {
		return (args) -> {
			// save a couple of customers
			tweetsRepository.save(new Tweet("pisTmp", LocalDate.now(), "Test"));

			// fetch all customers
			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			for (Tweet tweet : tweetsRepository.findAll()) {
				log.info(tweet.toString());
			}
			log.info("");
		};
	}


}
