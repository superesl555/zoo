package org.example;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.*;

// Интерфейсы
interface IAlive {
    int getFoodConsumption();
}

interface IInventory {
    int getNumber();
}

// Базовые классы
abstract class Animal implements IAlive {
    protected String name;
    protected int foodConsumption;

    public Animal(String name, int foodConsumption) {
        this.name = name;
        this.foodConsumption = foodConsumption;
    }

    @Override
    public int getFoodConsumption() {
        return foodConsumption;
    }

    public String getName() {
        return name;
    }
}

// Подклассы животных
abstract class Herbo extends Animal {
    protected int levelKidness;

    public Herbo(String name, int foodConsumption, int levelKidness) {
        super(name, foodConsumption);
        this.levelKidness = levelKidness;
    }

    public int getLevelKidness() {
        return levelKidness;
    }

    public void setLevelKidness(int levelKidness) {
        this.levelKidness = levelKidness;
    }
}

class Predator extends Animal {
    public Predator(String name, int foodConsumption) {
        super(name, foodConsumption);
    }
}

// Конкретные животные
class Monkey extends Herbo {
    public Monkey(String name, int levelKidness) {
        super(name, 5, levelKidness);
    }
}

class Rabbit extends Herbo {
    public Rabbit(String name, int levelKidness) {
        super(name, 2, levelKidness);
    }
}

class Tiger extends Predator {
    public Tiger(String name) {
        super(name, 10);
    }
}

class Wolf extends Predator {
    public Wolf(String name) {
        super(name, 8);
    }
}

// Классы инвентаря
abstract class Thing implements IInventory {
    protected int number;
    protected String name;

    public Thing(String name, int number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public int getNumber() {
        return number;
    }
}

class Table extends Thing {
    public Table(int number) {
        super("Table", number);
    }
}

class Computer extends Thing {
    public Computer(int number) {
        super("Computer", number);
    }
}

// Ветеринарная клиника
@Service
class VeterinaryClinic {
    public boolean checkHealth(Animal animal) {
        return new Random().nextBoolean();
    }
}

// Сервис зоопарка
@Service
class ZooService {
    private final VeterinaryClinic vetClinic;
    private final List<Animal> animals = new ArrayList<>();
    private final List<Thing> inventory = new ArrayList<>();

    public ZooService(VeterinaryClinic vetClinic) {
        this.vetClinic = vetClinic;
    }

    public void addAnimal(Animal animal) {
        if (vetClinic.checkHealth(animal)) {
            animals.add(animal);
            System.out.println(animal.getName() + " принят в зоопарк.");
        } else {
            System.out.println(animal.getName() + " не прошел проверку здоровья.");
        }
    }

    public void editAnimalKindness(String name, int newLevelKidness) {
        for (Animal animal : animals) {
            if (animal instanceof Herbo && animal.getName().equals(name)) {
                ((Herbo) animal).setLevelKidness(newLevelKidness);
                System.out.println("Уровень доброты у " + name + " изменен на " + newLevelKidness);
                return;
            }
        }
        System.out.println("Животное " + name + " не найдено или не является травоядным.");
    }

    public void addInventory(Thing thing) {
        inventory.add(thing);
    }

    public int getTotalFoodConsumption() {
        return animals.stream().mapToInt(Animal::getFoodConsumption).sum();
    }

    public List<Animal> getContactZooAnimals() {
        List<Animal> contactZooAnimals = new ArrayList<>();
        for (Animal animal : animals) {
            if (animal instanceof Herbo && ((Herbo) animal).getLevelKidness() >= 5) {
                contactZooAnimals.add(animal);
            }
        }
        return contactZooAnimals;
    }

    public void printAnimalCharacteristics(String name) {
        for (Animal animal : animals) {
            if (animal.getName().equals(name)) {
                System.out.println("Животное " + name + " потребляет: " + animal.getFoodConsumption());
            }
            if (animal instanceof Herbo && animal.getName().equals(name)) {
                System.out.println("Животное " + name + " имеет уровень доброты: " + ((Herbo) animal).getLevelKidness());
            }
        }
    }

    public void printInventory() {
        for (Thing thing : inventory) {
            System.out.println(thing.name + " - " + thing.getNumber());
        }
    }
}

// Конфигурация Spring
@Configuration
class AppConfig {
    @Bean
    public VeterinaryClinic veterinaryClinic() {
        return new VeterinaryClinic();
    }

    @Bean
    public ZooService zooService(VeterinaryClinic vetClinic) {
        return new ZooService(vetClinic);
    }
}

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ZooService zooService = context.getBean(ZooService.class);

        zooService.addAnimal(new Monkey("Chimp", 6));
        zooService.addAnimal(new Rabbit("Bunny", 4));
        zooService.addAnimal(new Tiger("Sherkhan"));
        zooService.addAnimal(new Wolf("Grey Wolf"));

        zooService.addInventory(new Table(101));
        zooService.addInventory(new Computer(202));

        System.out.println("Общее количество еды: " + zooService.getTotalFoodConsumption() + " кг");
        System.out.println("Животные для контактного зоопарка:");
        for (Animal animal : zooService.getContactZooAnimals()) {
            System.out.println(animal.getName());
        }

        System.out.println("Характеристики Bunny:");
        zooService.printAnimalCharacteristics("Bunny");

        System.out.println("Инвентарь зоопарка:");
        zooService.printInventory();

        zooService.editAnimalKindness("Bunny", 7);
        System.out.println("Животные для контактного зоопарка после изменения доброты:");
        for (Animal animal : zooService.getContactZooAnimals()) {
            System.out.println(animal.getName());
        }

        context.close();
    }
}
