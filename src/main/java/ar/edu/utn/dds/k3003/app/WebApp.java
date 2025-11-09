package ar.edu.utn.dds.k3003.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "ar.edu.utn.dds.k3003.controller",
        "ar.edu.utn.dds.k3003.config",
        "ar.edu.utn.dds.k3003.model",
        "ar.edu.utn.dds.k3003.repository",
        "ar.edu.utn.dds.k3003.app"
})
@EnableJpaRepositories(basePackages = "ar.edu.utn.dds.k3003.repository.jpa")
@EnableMongoRepositories(basePackages = "ar.edu.utn.dds.k3003.repository.mongo")
@EntityScan(basePackages = "ar.edu.utn.dds.k3003.model")
public class WebApp
{
    public static void main(String[] args)
    {
        SpringApplication.run(WebApp.class, args);
    }
}