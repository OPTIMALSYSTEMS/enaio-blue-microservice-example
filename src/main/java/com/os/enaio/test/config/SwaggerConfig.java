package com.os.enaio.test.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Created by cschulze on 28.11.2016.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("bi-service-api")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.os.enaio.bi.web"))
                .build()
                .securitySchemes(securitySchemes());
    }

    // @see:
    // https://github.com/springfox/springfox-demos/blob/master/boot-swagger/src/main/java/springfoxdemo/boot/swagger/Application.java
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("DEMO-Service API")
                .description("This Service is only used for demonstration purposes.")
                .termsOfServiceUrl("http://www.optimal-systems.de")
                // .licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")
                .version("1.0")
                .build();
    }

    private Predicate<String> apiPaths() {
        return or(regex("/.*"),
                regex("/bi.*"));
    }

    private List<SecurityScheme> securitySchemes(){
        List<SecurityScheme> response = new ArrayList<>();

        response.add(new BasicAuth("BASIC-Auth"));

        return response;
    }
}
