package org.example;

public class InhertanceMain {

    public static void main(String[] args) {

        ChildClass c= new ChildClass();
         c.myName();
        System.out.println(c.hashCode());
        Parent gc= new GrandChild();


    }
}
