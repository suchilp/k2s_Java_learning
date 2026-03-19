public class Fibonacci3R {
    public static void main(String[]args){
       Fibonacci3R f=new Fibonacci3R();
           int out=f.Fibonacci(15);
       System.out.println(out);
    }
    public int Fibonacci(int n){
        if(n<=1){
            return n;
        }
        return Fibonacci(n-1)+Fibonacci(n-2);
    }
}
