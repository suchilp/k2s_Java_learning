public class BSR {
    public static void main(String[] args) {
        int arr[] = {10, 20, 3, 40, 50, 60, 70, 80, 90, 100};
        int out = binarySearch(arr, 0, arr.length - 1, 70);
        System.out.println(out);
    }


    public static int binarySearch(int[] arr, int l, int h, int key) {
        if (l > h) {
            return -1;
        }
        int mid = (l + h) / 2;
        if (arr[mid] == key) {
            return mid;
        }
        if (arr[mid] < key) {
            return binarySearch(arr, l, mid + 1, key);
        } else {
            return binarySearch(arr, mid - 1, h, key);
        }
    }
}