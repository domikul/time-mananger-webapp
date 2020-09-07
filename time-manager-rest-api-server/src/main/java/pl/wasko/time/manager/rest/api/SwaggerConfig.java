package pl.wasko.time.manager.rest.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;

import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket get(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/**"))
                .build().apiInfo(createApiInfo());
    }

    private ApiInfo createApiInfo() {
        return new ApiInfo("Time Manager API",
                "documentation to Time Manager project implemented as part of the summer internship in WASKO",
                "1.00",
                "https://wasko.pl",
                new Contact("Dominika, Andrzej", "https://wasko.pl", "ppl.itr.2020@gmail.com" ),
                "own license",
                "https://wasko.pl",
                Collections.emptyList()
        );
    }

}
