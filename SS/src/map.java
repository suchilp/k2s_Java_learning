import java.util.Arrays;

public class map {
    public static void main(String []args){
        int[]arr={9,2,38,4,92,10,11,14};
        Arrays.stream(arr)
                .filter(a->a%2==1)
                .map(a->a+3)
                .boxed()
                .sorted((a,b)->b-a)

                .forEach(System.out::println);

    }
}
