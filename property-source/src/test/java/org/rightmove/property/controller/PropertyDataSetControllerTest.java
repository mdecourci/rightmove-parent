package org.rightmove.property.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rightmove.property.exceptions.DataNotFoundException;
import org.rightmove.property.exceptions.ServiceException;
import org.rightmove.property.model.Address;
import org.rightmove.property.model.Building;
import org.rightmove.property.model.Properties;
import org.rightmove.property.model.Property;
import org.rightmove.property.repository.PropertyDataSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(PropertyDataSetController.class)
class PropertyDataSetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PropertyDataSetRepository propertyDataSetRepository;

    private Properties properties;

    @BeforeEach
    void setUp() {
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
    void shouldGetPropertyResources() throws Exception {

       when(propertyDataSetRepository.find()).thenReturn(properties);

        mockMvc.perform(get("/propertiesdataset"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.properties", hasSize(1)))
                .andExpect(jsonPath("$.properties[0].id", is(1)))
                .andExpect(jsonPath("$.properties[0].price", is(1000000)))
                .andExpect(jsonPath("$.properties[0].building.bedrooms", is(7)))
                .andExpect(jsonPath("$.properties[0].building.bathrooms", is(2)))
                .andExpect(jsonPath("$.properties[0].address.houseNumber", is("12")))
                .andExpect(jsonPath("$.properties[0].address.address", is("Richard Lane")))
                .andExpect(jsonPath("$.properties[0].address.region", is("London")))
                .andExpect(jsonPath("$.properties[0].address.postcode", is("W1F 3FT")))
                .andExpect(jsonPath("$.properties[0].type", is("DETACHED")));

    }

    @Test
    void noDataFound() throws Exception {
        when(propertyDataSetRepository.find()).thenThrow(new DataNotFoundException());

        mockMvc.perform(get("/propertiesdataset"))
                .andExpect(status().isNotFound());

    }

    @Test
    void serverError() throws Exception {
        when(propertyDataSetRepository.find()).thenThrow(new ServiceException());

        mockMvc.perform(get("/propertiesdataset"))
                .andExpect(status().is5xxServerError());

    }
}