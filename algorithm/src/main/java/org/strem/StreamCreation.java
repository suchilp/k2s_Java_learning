package org.strem;

import java.util.*;
import java.util.stream.Stream;

public class StreamCreation {

    public static void main(String[] args) {

        Integer [] arr= {1,2,3,};

       Stream<Integer> stream= Arrays.stream(arr);
       Stream<Integer>  stream1=Stream.of(1,2,3,4,5);
       List<Integer> list= new ArrayList<>();
        Stream<Integer>  stream2= list.stream();
        Stream<Integer>  stream3=  Stream.<Integer>builder().add(1).add(2).add(3).build();

        Map<String,String> map = new HashMap<>();
        Stream<String>  stream4=   map.keySet().stream();
        map.values().stream();


    }
}
