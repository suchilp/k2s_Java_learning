package org.example;

public class Dog implements Animal{
    @Override
    public void eat() {
        System.out.println("Dog eats food");

    }

    @Override
    public void sleep() {
        System.out.println("Dog sleeps");
    }
}
