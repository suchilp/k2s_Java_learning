import java.util.Arrays;

public class StreamSource {
    public static void main(String[]args){
        int[]arr={1,2,3,4,5,6,7,8};
        Arrays.stream(arr)
                .filter(x->x<7)

                .forEach(System.out::println);
        }
    }

