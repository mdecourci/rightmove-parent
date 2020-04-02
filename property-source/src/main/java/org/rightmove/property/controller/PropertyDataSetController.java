package org.rightmove.property.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rightmove.property.model.Properties;
import org.rightmove.property.repository.PropertyDataSetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/propertiesdataset")
public class PropertyDataSetController {

    private final PropertyDataSetRepository propertyDataSetRepository;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Properties getAll() {
        log.info("getAll()");
        return propertyDataSetRepository.find();
    }
}
