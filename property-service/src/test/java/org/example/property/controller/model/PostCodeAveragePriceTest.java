package org.example.property.controller.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostCodeAveragePriceTest {

    @Test
    void canBuilder() {
        assertTrue(PostCodeAveragePrice.builder().build() instanceof PostCodeAveragePrice );
    }

    @Test
    void canGetCorrectPostcode() {
        assertEquals("GU30", PostCodeAveragePrice.builder().postcode("GU30").build().getPostcode());
    }

    @Test
    void canNotGetCorrectPostcode() {
        assertNotEquals("W12", PostCodeAveragePrice.builder().postcode("GU30").build().getPostcode());
    }

    @Test
    void canGetAveragePrice() {
        assertEquals(1111l, PostCodeAveragePrice.builder().averagePrice(1111d).build().getAveragePrice());
    }

    @Test
    void canNotGetAveragePrice() {
        assertNotEquals(2222l, PostCodeAveragePrice.builder().averagePrice(1111d).build().getAveragePrice());
    }

    @Test
    void testEquals() {
        var expectedostCodeAveragePrice = PostCodeAveragePrice.builder().averagePrice(1111d).postcode("GU30").build();
        var postCodeAveragePrice = PostCodeAveragePrice.builder().averagePrice(1111d).postcode("GU30").build();
        assertEquals(expectedostCodeAveragePrice, postCodeAveragePrice);
    }

    @Test
    void testNotEquals() {
        var unExpectedostCodeAveragePrice = PostCodeAveragePrice.builder().averagePrice(2222d).postcode("W12").build();
        var postCodeAveragePrice = PostCodeAveragePrice.builder().averagePrice(1111d).postcode("GU30").build();
        assertNotEquals(unExpectedostCodeAveragePrice, postCodeAveragePrice);
    }
}