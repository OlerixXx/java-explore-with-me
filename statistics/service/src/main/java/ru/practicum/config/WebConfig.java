package ru.practicum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.practicum.converter.CustomStringToLocalDateTimeConverter;

public class WebConfig implements WebMvcConfigurer {
    @Bean
    public CustomStringToLocalDateTimeConverter customStringToLocalDateTimeConverter() {
        return new CustomStringToLocalDateTimeConverter();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(customStringToLocalDateTimeConverter());
    }
}
