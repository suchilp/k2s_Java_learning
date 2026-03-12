public class BinarySearcExIt2 {
    public static void main(String args[]) {
        int arr[]={10,12,14,16,18,20,22,24,26,28,30};
        int out=binarySearch(arr,26);
        System.out.println(out);

    }

    public static int binarySearch(int arr[], int item) {
        int l = 0;
        int h = arr.length - 1;
        while (l <= h) {
            int mid = l + (h - 1) / 2;
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
