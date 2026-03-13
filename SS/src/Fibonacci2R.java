public class Fibonacci2R {
    public static void main(String[]args){
        Fibonacci2R f=new Fibonacci2R();
        int myobj=f.fibonacci(50);
        System.out.println(myobj);

    }
    public int fibonacci(int n){
        if(n<=1){
            return n;
        }
        return fibonacci(n-1)+fibonacci(n-2);
    }
}
