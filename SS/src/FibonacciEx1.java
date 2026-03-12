public class FibonacciEx1 {
    public static void main(String[]args){
        FibonacciEx1 f=new FibonacciEx1();
        int out=f.fibonacci(10);
        System.out.println(out);

    }
    public int fibonacci(int n){
        if(n<=1){
            return n;
        }
        return fibonacci(n-1)+fibonacci(n-2);
    }
}
