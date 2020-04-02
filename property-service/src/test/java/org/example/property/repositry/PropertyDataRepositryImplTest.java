package org.example.property.repositry;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.property.exceptions.DataNotFoundException;
import org.example.property.exceptions.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@SpringBootTest
class PropertyDataRepositryImplTest {

    @Autowired
    private PropertyDataRepositryImpl propertyDataRepositry;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private MockRestServiceServer mockServer;

    @Value("${property.endpoint.url}")
    private String url;

    private Properties properties;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        properties = Properties.builder()
                .properties(List.of(Property.builder()
                        .id(1l)
                        .price(1000000l)
                        .building(Building.builder()
                                .bathrooms(2)
                                .bedrooms(7)
                                .build())
                        .address(Address.builder()
                                .houseNumber("12")
                                .address("Richard Lane")
                                .region("London")
                                .postcode("W1F 3FT")
                                .build())
                        .type("DETACHED")
                        .build()))
                .build();
    }

    @Test
    void canFindProperties() throws Exception {

        String propertiesJson = objectMapper.writeValueAsString(properties);

        mockServer.expect(ExpectedCount.once(),requestTo(new URI(url)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body(propertiesJson)
                );

        var properties = new ArrayList<Property>(propertyDataRepositry.findProperties());
        assertEquals(properties.get(0).getId(), 1);
        assertEquals(properties.get(0).getPrice(), 1000000);
        assertEquals(properties.get(0).getType(), "DETACHED");
        assertEquals(properties.get(0).getBuilding().getBedrooms(), 7);
        assertEquals(properties.get(0).getBuilding().getBathrooms(), 2);
        assertEquals(properties.get(0).getAddress().getHouseNumber(), "12");
        assertEquals(properties.get(0).getAddress().getAddress(), "Richard Lane");
        assertEquals(properties.get(0).getAddress().getRegion(), "London");
        assertEquals(properties.get(0).getAddress().getPostcode(), "W1F 3FT");
    }

    @Test
    void shouldFindEmptyPropertiesFromEndpointWithEmptyJSONArray() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(url)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body("{\"properties\":[]}")
                );
        assertTrue(propertyDataRepositry.findProperties().isEmpty());
    }

    @Test
    void shouldFindEmptyPropertiesEndpointWithEmptyJSON() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(url)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body("{}")
                );
        assertTrue(propertyDataRepositry.findProperties().isEmpty());
    }

    @Test
    void shouldThrowExceptionForDataNotFound() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(url)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThrows(DataNotFoundException.class, () -> {
            propertyDataRepositry.findProperties();
        });
    }

    @Test
    void shouldThrowExceptionForServerError() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(url)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());

        assertThrows(ServiceException.class, () -> {
            propertyDataRepositry.findProperties();
        });
    }

    @Test
    void shouldThrowExceptionForRemoteException() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(url)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withException(new IOException()));

        assertThrows(ServiceException.class, () -> {
            propertyDataRepositry.findProperties();
        });
    }
}