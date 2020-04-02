package org.example.property.controller;

import org.example.property.controller.model.PropertyType;
import org.example.property.exceptions.DataNotFoundException;
import org.example.property.exceptions.ServiceException;
import org.example.property.service.PropertyServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PropertyServiceController.class)
class PropertyServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PropertyServiceImpl propertyService;
    @MockBean
    private PropertyValidation propertyValidation;

    @Autowired
    private PropertyServiceController propertyServiceController;

    @Test
    public void shouldCalculateTheAveragePriceForPropertiesAroundPostCode() throws Exception {
        var postCode = "W1F";
        var averagePrice = 1000000d;

        when(propertyService.calculateAveragePriceAroundPostCode(postCode)).thenReturn(averagePrice);

        mockMvc.perform(get("/properties/averageprice/{postCode}", postCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postcode", is("W1F")))
                .andExpect(jsonPath("$.averagePrice", is(averagePrice)))
                .andReturn();

        verify(propertyValidation).validatePostCode(postCode);
    }

    @Test
    public void postCodeBadRequestFormat() throws Exception {
        var postCode = "111 A";

        doThrow(IllegalArgumentException.class).when(propertyValidation).validatePostCode(postCode);

        mockMvc.perform(get("/properties/averageprice/{postCode}", postCode))
                .andExpect(status().isBadRequest()).andReturn();

        verifyNoInteractions(propertyService);
    }

    @Test
    public void blankPostCodeUrlNotFound() throws Exception {
        var postCode = "";

        mockMvc.perform(get("/properties/averageprice/{postCode}", postCode))
                .andExpect(status().isNotFound()).andReturn();

        verifyNoInteractions(propertyValidation);
        verifyNoInteractions(propertyService);
    }

    @Test
    public void invalidTypePostCodeBadRequest() throws Exception {
        var postCode = "10";

        doThrow(IllegalArgumentException.class).when(propertyValidation).validatePostCode(postCode);

        mockMvc.perform(get("/properties/averageprice/{postCode}", postCode))
                .andExpect(status().isBadRequest()).andReturn();

        verifyNoInteractions(propertyService);

    }

    @Test
    public void postCodeNotFound() throws Exception {
        var postCode = "Z1Z";

        when(propertyService.calculateAveragePriceAroundPostCode(postCode)).thenThrow(DataNotFoundException.class);

        mockMvc.perform(get("/properties/averageprice/{postCode}", postCode))
                .andExpect(status().isNotFound()).andReturn();

        verify(propertyValidation).validatePostCode(postCode);
    }

    @Test
    public void serviceErrorWithPostCode() throws Exception {
        var postCode = "W1F";

        when(propertyService.calculateAveragePriceAroundPostCode(postCode)).thenThrow(ServiceException.class);

        mockMvc.perform(get("/properties/averageprice/{postCode}", postCode))
                .andExpect(status().isInternalServerError()).andReturn();

        verify(propertyValidation).validatePostCode(postCode);
    }

    @Test
    public void shouldCalculateTheDifferenceInAveragePriceOfPropertyTypes() throws Exception {
        var fromType = "DETACHED";
        var toType = "FLAT";
        var averagePrice = Double.valueOf(1000000l);

        when(propertyService.calculateDifferenceInAveragePriceForTypes(PropertyType.valueOf(fromType), PropertyType.valueOf(toType))).thenReturn(averagePrice);

        mockMvc.perform(get("/properties/price/difference/{fromType}/{toType}", fromType, toType))
                .andExpect(status().isOk())
                .andExpect(content().string(averagePrice.toString()))
                .andReturn();

        verify(propertyValidation).validateType(fromType);
        verify(propertyValidation).validateType(toType);
    }

    @Test
    public void propertyTypeBadRequest() throws Exception {
        var fromType = "11";
        var toType = "11";

        doThrow(IllegalArgumentException.class).when(propertyValidation).validateType(fromType);

        mockMvc.perform(get("/properties/price/difference/{fromType}/{toType}", fromType, toType))
                .andExpect(status().isBadRequest())
                .andReturn();

        verifyNoInteractions(propertyService);
    }

    @Test
    public void blankPropertyTypeUrlNotFound() throws Exception {
        var fromType = "";
        var toType = "";

        mockMvc.perform(get("/properties/price/difference/{fromType}/{toType}", fromType, toType))
                .andExpect(status().isNotFound())
                .andReturn();

        verifyNoInteractions(propertyValidation);
        verifyNoInteractions(propertyService);
    }

    @Test
    public void serviceErrorWithPropertyType() throws Exception {
        var fromType = "DETACHED";
        var toType = "FLAT";

        when(propertyService.calculateDifferenceInAveragePriceForTypes(PropertyType.valueOf(fromType), PropertyType.valueOf(toType)))
                .thenThrow(ServiceException.class);

        mockMvc.perform(get("/properties/price/difference/{fromType}/{toType}", fromType, toType))
                .andExpect(status().isInternalServerError())
                .andReturn();

        verify(propertyValidation).validateType(fromType);
        verify(propertyValidation).validateType(toType);
    }
}