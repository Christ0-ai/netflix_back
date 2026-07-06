package com.netflix.api.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;

@Configuration
public class I18nConfig {

    @Bean
    MessageSourceAccessor messages(MessageSource messageSource) {
        return new MessageSourceAccessor(messageSource);
    }
}