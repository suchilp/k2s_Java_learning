package org.search;

public class BSIterative {
    public static void main(String[] args) {
        int arr[] = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160};
        int out = binarySearchItt(20, arr);
        System.out.println(out);
    }
    public static int binarySearchItt(int item, int arr[]) {
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
