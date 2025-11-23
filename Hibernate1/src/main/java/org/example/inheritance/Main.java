package org.example.inheritance;

public class Main {
    public static void main(String[] args) {
       Dog g = new Dog();
       g.bark();
       g.dogBarking();

       Cat c = new Cat();
       c.bark();
       c.catBarking();


        Animal animal1 = new Dog();
      //  animal1.bark();
        // animal1.dogBarking();

        Animal animal = new Cat();
       // animal.bark();
       // animal.catBarking();

        Animal animal3 = new Animal();


        System.out.println(g.name);
        System.out.println(animal1.name);

        //Flower f= new Rose();
      //  f.name();


    }
}
