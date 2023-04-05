package pro.sky.teamwork.animalsheltertelegrambotv2;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition
public class AnimalShelterTelegramBotV2Application {

    public static void main(String[] args) {
        SpringApplication.run(AnimalShelterTelegramBotV2Application.class, args);
    }

}
