package cn.schff.dyvlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableOpenApi
@EnableWebMvc
public class DyVlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(DyVlogApplication.class, args);
    }

}
