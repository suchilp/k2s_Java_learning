package org.strem;

import java.util.ArrayList;
import java.util.List;

public class StreamExample {
    public static void main(String[] args) {


        List<Integer> l=List.of(1,1,12,3,4,15,6,70,8,9,10);
       l.stream().filter(n-> n%2==0).forEach(System.out::println);  //Even Number

        l.stream().map(x-> x*x).forEach(System.out::println);  //Square

        //count

        long count=l.stream().count();
        l.stream().distinct().forEach(System.out::println);

        l.stream().max(Integer::compare);


        List<Employee> list = new ArrayList<>();
        Employee obj = new Employee();
        obj.setName("Suchil");
        obj.setSalary(100);
        list.add(obj);
        for(Employee e :list)
        {
            System.out.println(e.getSalary());
        }

        list.stream().map(e ->e.getSalary()).forEach(System.out::println);

    }


}
