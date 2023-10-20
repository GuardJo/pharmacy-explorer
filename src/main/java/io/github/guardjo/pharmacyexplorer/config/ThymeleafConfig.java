package io.github.guardjo.pharmacyexplorer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

@Configuration
public class ThymeleafConfig {
    @Bean
    public SpringResourceTemplateResolver thymeleafResolver(SpringResourceTemplateResolver templateResolver) {
        templateResolver.setUseDecoupledLogic(true); // Thymeleaf decoupleLogic Enable

        return templateResolver;
    }
}
