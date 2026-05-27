package org.strem;

import java.util.Arrays;
import java.util.stream.Stream;

public class HelloStream {

    public static void main(String[] args) {

        Integer[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10,11};

        for(Integer value: arr)
        {
            System.out.println(value);
        }

        Arrays.stream(arr).forEach(System.out::println);

        long count=Arrays.stream(arr).count();
        System.out.println(count);
        Arrays.stream(arr).filter(i-> i%2==0).forEach(System.out::println);
        Arrays.stream(arr).filter(i-> i%2==0).count();
        Arrays.stream(arr).filter(i-> i%2==0).toArray();



    }
}
