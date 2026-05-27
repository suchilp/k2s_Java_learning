package org.strem;

public class Addition  implements  AdditionInterface{
    @Override
    public int add(int a, int b) {
       int c= a+b;
       return c;
    }
}
