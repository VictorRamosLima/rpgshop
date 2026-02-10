package rpgshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RpgshopApplication {
    public static void main(final String[] args) {
        SpringApplication.run(RpgshopApplication.class, args);
    }
}
