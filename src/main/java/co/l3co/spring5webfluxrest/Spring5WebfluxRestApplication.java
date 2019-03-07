package co.l3co.spring5webfluxrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class Spring5WebfluxRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(Spring5WebfluxRestApplication.class, args);
    }

}
