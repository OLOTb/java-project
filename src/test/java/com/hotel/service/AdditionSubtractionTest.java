package com.hotel.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AdditionSubtractionTest {

    @Test
    void shouldAddTwoNumbersCorrectly() {
        int a = 2;
        int b = 3;
        int result = a + b;
        assertEquals(5, result);
    }

    @Test
    void shouldSubtractTwoNumbersCorrectly() {
        int a = 10;
        int b = 4;
        int result = a - b;
        assertEquals(6, result);
    }
}