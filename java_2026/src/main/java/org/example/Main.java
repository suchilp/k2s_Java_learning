package org.example;

public class Main {

    public Main()
    {
        System.out.println("My default constructor");
    }

    //properties/variable

    //method/function

    //  int a;// Declaration
    //   a=10; // Initialization

    int myVillageNumber = 20;
   private static int  a;
    void printName() {
        System.out.println("Suchil");
    }

     static void printNameSamaira() {
        System.out.println("Samaira");
    }
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Main m = new Main();
        m.printName();
        m.myVillageNumber=30;

        printNameSamaira();
        System.out.println(a);
        System.out.println(m.myVillageNumber);
        m.a=20;
        System.out.println(m.a);

        Main m1 = new Main();
        System.out.println("-------m1 "+m1.myVillageNumber);
        System.out.println(m1.a);

        Main m3 = new Main();
        System.out.println(m3.myVillageNumber);//20
        System.out.println(m3.a);

    }
}