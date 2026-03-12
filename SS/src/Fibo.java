public class Fibo {
    public static void main (String[]args){
        Fibo f=new Fibo();
        int out=f.fibonacci(6);
        System.out.println(out);

    }
    public int fibonacci(int n){
        if(n<=1){
            return n;
        }
        return fibonacci(n-1)+fibonacci(n+1);
    }
}
