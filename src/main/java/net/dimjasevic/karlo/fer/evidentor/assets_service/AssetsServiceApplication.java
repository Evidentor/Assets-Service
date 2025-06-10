package net.dimjasevic.karlo.fer.evidentor.assets_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("net.dimjasevic.karlo.fer.evidentor.*")
@ComponentScan(basePackages = {"net.dimjasevic.karlo.fer.evidentor.*"})
@EntityScan("net.dimjasevic.karlo.fer.evidentor.*")
public class AssetsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssetsServiceApplication.class, args);
	}

}
