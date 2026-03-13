public class BinaryS1 {
    public static void main(String[] args) {
        int arr[]={2,3,4,5,69,10,23,45,67,94};
        int out=binarySearchItt(arr,23);
System.out.println(out);
    }

    public static int binarySearchItt(int arr[], int item) {
        int l = 0;
        int h = arr.length - 1;
        while (l <= h) {
            int mid = l + (h - l) / 2;
            if (arr[mid] == item) {
                return mid;
            }
            if (arr[mid] < item) {
                l = mid + 1;
            } else {
                h = mid - 1;
            }

        }
return -1;
    }

}