package by.belstu.lab02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"by.belstu.lab02.repositories"})
public class Lab02Application {

	public static void main(String[] args) {
		SpringApplication.run(Lab02Application.class, args);
	}

}
