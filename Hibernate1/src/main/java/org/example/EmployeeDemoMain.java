package org.example;

import java.util.ArrayList;
import java.util.Collections;

public class EmployeeDemoMain {
    public static void main(String[] args) {
        System.out.println("Employee main");

        EmployeeDemo obj= new EmployeeDemo();
        obj.setEmpName("Suchil");
        obj.setDesignation("Lead Software Developer");
        obj.setEmpId(1);


        EmployeeDemo obj1= new EmployeeDemo();
        obj1.setEmpName("Pina");
        obj1.setDesignation("1Software Developer");
        obj1.setEmpId(2);

        EmployeeDemo obj3= new EmployeeDemo();
        obj3.setEmpName("Swati");
        obj3.setDesignation("2Software Developer");
        obj3.setEmpId(3);

        ArrayList<EmployeeDemo> list = new ArrayList<>();
        list.add(obj3);
        list.add(obj1);
        list.add(obj);

        System.out.println("Before compare"+list);
        Collections.sort(list,new IdComparator());
        System.out.println("After compare"+list);
        Collections.sort(list,new DesignationComparator());
        System.out.println("After compare"+list);
        Collections.sort(list,new NameComparator());
        System.out.println("After compare"+list);



    }
}
