package com.alfheim.aflheim_community.converter.config;

import com.alfheim.aflheim_community.converter.publication.PublicationDtoToPublicationConverter;
import com.alfheim.aflheim_community.converter.publication.PublicationToPublicationDtoConverter;
import com.alfheim.aflheim_community.converter.publication.StringToPublicationFormConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

@Configuration
public class ConversionServiceConfig {

    @Bean
    public ConversionService conversionService() {
        var conversionService = new DefaultConversionService();
        conversionService.addConverter(new PublicationDtoToPublicationConverter());
        conversionService.addConverter(new PublicationToPublicationDtoConverter());
        conversionService.addConverter(new StringToPublicationFormConverter());

        return conversionService;
    }
}
