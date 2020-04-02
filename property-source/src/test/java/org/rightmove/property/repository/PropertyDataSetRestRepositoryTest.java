package org.rightmove.property.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rightmove.property.exceptions.DataNotFoundException;
import org.rightmove.property.exceptions.ServiceException;
import org.rightmove.property.model.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@SpringBootTest
public class PropertyDataSetRestRepositoryTest {
    @Autowired
    private PropertyDataSetRestRepository propertyDataSetRestRepository;

    @Autowired
    private RestTemplate restTemplate;


    private MockRestServiceServer mockServer;

    @Value("classpath:test_property.json")
    private Resource fileUrlResource;

    @Value("${property.dataset.url}")
    private String url;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void shouldFindPropertiesFromRemoteEndpoint() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(url)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body(fileUrlResource)
                );

        Properties properties = propertyDataSetRestRepository.find();
        assertEquals(properties.getProperties().get(0).getId(), 1);
        assertEquals(properties.getProperties().get(0).getPrice(), 1000000);
        assertEquals(properties.getProperties().get(0).getType(), "DETACHED");
        assertEquals(properties.getProperties().get(0).getBuilding().getBedrooms(), 7);
        assertEquals(properties.getProperties().get(0).getBuilding().getBathrooms(), 2);
        assertEquals(properties.getProperties().get(0).getAddress().getHouseNumber(), "12");
        assertEquals(properties.getProperties().get(0).getAddress().getAddress(), "Richard Lane");
        assertEquals(properties.getProperties().get(0).getAddress().getRegion(), "London");
        assertEquals(properties.getProperties().get(0).getAddress().getPostcode(), "W1F 3FT");
    }

    @Test
    void shouldFindEmptyPropertiesFromRemoteEndpointWithEmptyJSONArray() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(url)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body("{\"properties\":[]}")
                );
        assertTrue(propertyDataSetRestRepository.find().getProperties().isEmpty());
    }

    @Test
    void shouldFindEmptyPropertiesFromRemoteEndpointWithEmptyJSON() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(url)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body("{}")
                );
        assertTrue(propertyDataSetRestRepository.find().getProperties().isEmpty());
    }

    @Test
    void shouldThrowExceptionForDataNotFound() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(url)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThrows(DataNotFoundException.class, () -> {
            propertyDataSetRestRepository.find();
        });
    }

    @Test
    void shouldThrowExceptionForServerError() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(url)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());

        assertThrows(ServiceException.class, () -> {
            propertyDataSetRestRepository.find();
        });
    }

    @Test
    void shouldThrowExceptionForRemoteException() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(url)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withException(new IOException()));

        assertThrows(ServiceException.class, () -> {
            propertyDataSetRestRepository.find();
        });
    }
}