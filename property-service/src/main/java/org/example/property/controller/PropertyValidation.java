package org.example.property.controller;

import org.apache.commons.lang3.StringUtils;
import org.example.property.controller.model.PropertyType;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PropertyValidation {
    public void validatePostCode(String postCode) {
        Optional.ofNullable(StringUtils.trim(postCode))
                .filter((value) -> !StringUtils.containsWhitespace(value))
                .filter(StringUtils::isAlphanumeric)
                .filter((firstChar) -> Character.isAlphabetic(firstChar.toCharArray()[0]))
                .orElseThrow(IllegalArgumentException::new);
    }

    public void validateType(String type) {
        PropertyType.find(type);
    }
}
