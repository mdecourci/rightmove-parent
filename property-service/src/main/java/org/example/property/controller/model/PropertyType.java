package org.example.property.controller.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public enum PropertyType {
    DETACHED, TERRACED, FLAT, SEMI_DETACHED;

    public static Optional<PropertyType> find(String type) {
        return Optional.ofNullable(valueOf(StringUtils.trim(type)));
    }
}
