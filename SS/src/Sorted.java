import java.util.Arrays;

public class Sorted {
    public static void main(String[]args){
        int[]arr={2,5,4,16,8,10,9,12,18,14};
        Arrays.stream(arr)
               .sorted()
                .boxed()
                .sorted((a,b)-> b-a)
                .forEach(System.out::println);
    }
}
