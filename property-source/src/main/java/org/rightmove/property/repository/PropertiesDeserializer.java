package org.rightmove.property.repository;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.rightmove.property.model.Address;
import org.rightmove.property.model.Building;
import org.rightmove.property.model.Properties;
import org.rightmove.property.model.Property;

import java.io.IOException;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PropertiesDeserializer extends StdDeserializer<Properties> {
    public PropertiesDeserializer(Class<?> vc) {
        super(vc);
    }

    public PropertiesDeserializer() {
        this(null);
    }

    @Override
    public Properties deserialize(JsonParser jsonParser,
                                  DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node =
                jsonParser.getCodec().readTree(jsonParser);

        JsonNode propertiesNode = node.get("properties");

        List<Property> propertyList = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(propertiesNode.iterator(), Spliterator.ORDERED),
                false).map(propertyNode -> {
            return Property.builder()
                    .id(propertyNode.get("id").asLong())
                    .price(propertyNode.get("price").asLong())
                    .building(Building.builder()
                            .bedrooms(propertyNode.get("bedrooms").asInt())
                            .bathrooms(propertyNode.get("bathrooms").asInt())
                            .build())
                    .address(Address.builder()
                            .houseNumber(propertyNode.get("houseNumber").asText())
                            .address(propertyNode.get("address").asText())
                            .postcode(propertyNode.get("postcode").asText())
                            .region(propertyNode.get("region").asText())
                            .build())
                    .type(propertyNode.get("type").asText())
                    .build();
        }).collect(Collectors.toList());

        return Properties.builder().properties(propertyList).build();
    }
}
