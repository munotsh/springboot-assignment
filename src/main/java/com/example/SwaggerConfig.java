package com.example;
import com.google.common.base.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import static springfox.documentation.builders.PathSelectors.regex;
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api()
    {
        return  new Docket(DocumentationType.SWAGGER_2).
                apiInfo(metadat()).select().paths(apiPaths()).build();
    }
    private Predicate<String> apiPaths() {
        return regex("com.example.assignment.controller");
    }
    private ApiInfo metadat() {
        return new ApiInfoBuilder().
                title("springboot-assignment-ws").
                description("springboot assignment WS").
                version("1.0").
                build();
    }
    @Bean
    public UiConfiguration uiConfig()
    {
        return UiConfigurationBuilder.builder().validatorUrl(StringUtils.EMPTY).build();
    }
}
