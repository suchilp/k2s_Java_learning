import java.util.Arrays;

public class StreamExamples{
    public static void main(String[]args){
        int[]arr={10,11,12,13,14,15,16,17,18,19,20};
        Arrays.stream(arr)
                .map(x->x*2)
                .forEach(System.out::println);
    }
}

