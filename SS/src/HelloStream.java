import java.util.Arrays;
public class HelloStream {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        // for (int i : arr) {
        //    if (i % 2== 1) {

        //        System.out.println(i);
        //   }
        //  }
        Arrays.stream(arr)
                .filter(n -> n % 2 == 1)
                .forEach(System.out::println);

    }
}