package org.swati;

 class Pen{
    String color;
    String type;
    public void write(){
        System.out.println("Writting something");
    }
}




public class Oops{
public static void main(String args[]){
    Pen p=new Pen();
    p.color="blue";
    p.type="gel";
    p.write();
}
}
