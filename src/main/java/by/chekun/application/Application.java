package by.chekun.application;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "by.chekun")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
