package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ZooServiceTest {
    private ZooService zooService;
    private VeterinaryClinic vetClinicMock;

    @BeforeEach
    void setUp() {
        vetClinicMock = Mockito.mock(VeterinaryClinic.class);
        zooService = new ZooService(vetClinicMock);
    }

    @Test
    void testAddAnimal_WhenHealthy_ShouldBeAdded() {
        // Устанавливаем, что проверка здоровья возвращает true
        when(vetClinicMock.checkHealth(any(Animal.class))).thenReturn(true);

        int oldConsumption = zooService.getTotalFoodConsumption();
        Monkey monkey = new Monkey("Chimp", 7);
        zooService.addAnimal(monkey);

        assertEquals(oldConsumption + 5, zooService.getTotalFoodConsumption()); // +5, потому что обезьянам в среднем нужно +5
    }

    @Test
    void testAddAnimal_WhenUnhealthy_ShouldNotBeAdded() {
        when(vetClinicMock.checkHealth(any(Animal.class))).thenReturn(false);

        Rabbit rabbit = new Rabbit("Bunny", 5);
        zooService.addAnimal(rabbit);

        assertEquals(0, zooService.getTotalFoodConsumption()); // Животное не добавилось
    }


    @Test
    void testEditAnimalKindness_ShouldUpdateKindnessLevel() {
        when(vetClinicMock.checkHealth(any(Animal.class))).thenReturn(true);
        Monkey monkey = new Monkey("Chimp", 4);
        zooService.addAnimal(monkey);

        zooService.editAnimalKindness("Chimp", 9);

        List<Animal> contactZooAnimals = zooService.getContactZooAnimals();
        assertEquals(1, contactZooAnimals.size());
        assertEquals("Chimp", contactZooAnimals.get(0).getName());
        assertEquals(9, ((Herbo) contactZooAnimals.get(0)).getLevelKidness());
    }

    @Test
    void testGetTotalFoodConsumption() {
        when(vetClinicMock.checkHealth(any(Animal.class))).thenReturn(true);

        zooService.addAnimal(new Tiger("Sherkhan"));
        zooService.addAnimal(new Rabbit("Bunny", 3));

        assertEquals(10 + 2, zooService.getTotalFoodConsumption());
    }

    @Test
    void testGetContactZooAnimals() {
        when(vetClinicMock.checkHealth(any(Animal.class))).thenReturn(true);

        zooService.addAnimal(new Monkey("Chimp", 6));
        zooService.addAnimal(new Rabbit("Bunny", 4));
        zooService.addAnimal(new Tiger("Sherkhan"));

        List<Animal> contactZooAnimals = zooService.getContactZooAnimals();
        assertEquals(1, contactZooAnimals.size());
        assertEquals("Chimp", contactZooAnimals.get(0).getName());
    }
}
