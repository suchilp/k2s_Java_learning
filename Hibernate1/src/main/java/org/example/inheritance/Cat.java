package org.example.inheritance;

public class Cat extends  Animal{

    String name="Cat";
    public void catBarking()
    {
        System.out.println("Cat Barking Meown ");
    }

    @Override
    public void bark(){
        System.out.println(" Cat Animal Barking");
    }
}
