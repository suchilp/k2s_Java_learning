import java.util.Arrays;

public class EvenNumbers {

    static void main() {
        System.out.println("suchil");

        int [] arr={1,2,3,4,5,6,7,8,9,10};

       int out[]= even(arr);
        System.out.println(Arrays.toString(out));
        //out put {2,4,6,8,10}
    }


    public static int [] even(int []input )
    {
        int out[]= new int[input.length];
        int j=0;

        for(int i=0;i<input.length;i++)
        {
            if(input[i]%2==0)
            {
                out[j]=input[i];
                j++;
            }
        }
        return out;


    }

}
