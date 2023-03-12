package com.laurence.chatbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final String DEFAULT_INCLUDE_PATTERN_CHAT = "/chatbot/v1/chat/.*";
    public static final String DEFAULT_INCLUDE_PATTERN_TRANSACTION = "/chatbot/v1/transaction/.*";
    public static final String DEFAULT_INCLUDE_PATTERN_PROMO = "/chatbot/v1/promo/list";

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.laurence.chatbot"))
                .paths(PathSelectors.any())
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .operationSelector(o -> o.requestMappingPattern().matches(DEFAULT_INCLUDE_PATTERN_CHAT) || o.requestMappingPattern().matches(DEFAULT_INCLUDE_PATTERN_TRANSACTION) ||
                        o.requestMappingPattern().matches(DEFAULT_INCLUDE_PATTERN_PROMO))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "ChatBot",
                "Simulator Chatbot",
                "1.0",
                "Terms of service",
                new Contact("Laurence Butar-butar", "https://wa.me/6281281612767", "laurencebutarbutar@gmail.com"),
                "License of API",
                "API license URL",
                Collections.emptyList());
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }
}
