public class FIboItt2 {
    public static void main(String[]args){
        FIboItt2 f=new FIboItt2();
        int out=f.fibonacci(15);
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
