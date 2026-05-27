package org.strem;

import java.util.Arrays;

public class HelloEven {
    public static void main(String[] args) {

        int arr[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};





       int [] arr1=Arrays.stream(arr).filter(x-> x%2==1).toArray();

        for(int i: arr1)
        {
            System.out.println(i);

        }

        int [] out=getEvenNumberArry(arr);
        System.out.println(out);
        for(int i: out)
        {
            if(i>0) {
                System.out.println(i);
            }
        }
    }

    public static int[] getEvenNumberArry(int arr[]) {
        int[] output = new int[arr.length];
        int j = 0;

        for (int i = 0; i < arr.length; i++) {
            int val = arr[i];
            if (val % 2 == 1) {
                output[j] = val;
                j++;
            }


        }
        return output;

    }
}
