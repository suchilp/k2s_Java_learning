package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmployeeMain {

    public static void main(String[] args) {

        System.out.println("Employee main");

        Employee obj= new Employee();
        obj.setEmpName("Suchil");
        obj.setDesignation("Lead Software Developer");
        obj.setEmpId(1);


        Employee obj1= new Employee();
        obj1.setEmpName("Pina");
        obj1.setDesignation("Software Developer");
        obj1.setEmpId(2);

        Employee obj3= new Employee();
        obj3.setEmpName("Swati");
        obj3.setDesignation("Software Developer");
        obj3.setEmpId(3);

        Employee obj4= new Employee();
        obj4.setEmpName("A");
        obj4.setDesignation("1Software Developer");
        obj4.setEmpId(4);

        Employee obj5= new Employee();
        obj5.setEmpName("B");
        obj5.setDesignation("2Software Developer");
        obj5.setEmpId(5);

        ArrayList<Employee> list = new ArrayList<>();
        list.add(obj3);
        list.add(obj1);
        list.add(obj);
        list.add(obj4);
        list.add(obj5);

        System.out.println("Before compare"+list);
       Collections.sort(list);
      System.out.println(list);



    }
}
