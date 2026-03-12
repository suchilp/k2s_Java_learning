public class FibonacciEx1 {
    public static void main(String[]args){
        FibonacciEx1 f=new FibonacciEx1();
        int out=f.fibonacci(16);
        System.out.println(out);

    }
    public int fibonacci(int n){
        if(n<=1){
            return n;
        }
        int a=0;
        int b=1;
        for(int i=2;i<=n;i++){
          int c=a+b;
          a=b;
          b=c;
        }
        return b;
    }
}
