package org.rightmove.property;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.rightmove.property.model.Properties;
import org.rightmove.property.repository.PropertiesDeserializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Slf4j
public class PropertyDataSetApplication {
    public static void main(String[] args) {
        SpringApplication.run(PropertyDataSetApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        var objectMapper = new ObjectMapper();

        var module = new SimpleModule();
        module.addDeserializer(Properties.class, new PropertiesDeserializer());
        objectMapper.registerModule(module);

        return objectMapper;
    }
}
