package org.swati;

public class Addition {
    public static void main(String[] args) {
        Addition a=new Addition();
      int c=  a.add(200,300);
        System.out.println(c);
    }
    public int add(int a ,int b){
        return a+b;
    }
}
