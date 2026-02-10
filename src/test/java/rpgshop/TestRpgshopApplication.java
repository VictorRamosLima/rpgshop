package rpgshop;

import org.springframework.boot.SpringApplication;
import org.testcontainers.utility.TestcontainersConfiguration;

public class TestRpgshopApplication {
    public static void main(String[] args) {
        SpringApplication.from(RpgshopApplication::main).with(TestcontainersConfiguration.class).run(args);
    }
}
