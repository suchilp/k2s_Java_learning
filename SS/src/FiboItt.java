public class FiboItt {
    public static void main(String[] args) {
        FiboItt f=new FiboItt();
        int n=f.fibonacci(30);
        System.out.println(n);

    }

    public int fibonacci(int n) {
        if (n <= 1) {
            return n;
        }

        int a = 0;
        int b = 1;
        for (int i = 2; i <= n; i++) {


        int c=a+b;
        a=b;
        b=c;
    }
    return b;
}
}