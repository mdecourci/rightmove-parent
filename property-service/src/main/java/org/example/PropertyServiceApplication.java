package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class PropertyServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PropertyServiceApplication.class, args);
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        var objectMapper = new ObjectMapper();
//
//        var module = new SimpleModule();
//        module.addDeserializer(Properties.class, new PropertiesDeserializer());
//        objectMapper.registerModule(module);
//
        return objectMapper;
    }

}
