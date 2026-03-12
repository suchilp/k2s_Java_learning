package org.test;


public class MyFibonacci {
    public static void main(String[] args) {
        MyFibonacci myFibonacci = new MyFibonacci();
        int out = myFibonacci.fibonacciItt(10);
        System.out.println(out);
    }
    public int fibonacci(int n) {
        if (n <= 1) {
            return n;
        } else {
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }
    public int fibonacciItt(int n) {
        if (n <= 1) {
            return n;
        }
        int a = 0;
        int b = 1;
        for (int i = 2; i <= n; i++) {
            int c = a + b;
            a = b;
            b = c;
        }
        return b;
    }

}
