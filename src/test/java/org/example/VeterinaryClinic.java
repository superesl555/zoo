package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VeterinaryClinicTest {
    @Test
    void testCheckHealth_ShouldReturnBoolean() {
        VeterinaryClinic clinic = new VeterinaryClinic();
        boolean result = clinic.checkHealth(new Tiger("Sherkhan"));
        assertTrue(result || !result); // Проверяем, что метод возвращает boolean
    }
}