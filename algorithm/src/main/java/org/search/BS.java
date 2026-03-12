package org.search;

public class BS {
    public static void main(String[] args) {
        int arr[] = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160};
        int out = binarySearch(0, arr.length-1, 100, arr);
        System.out.println("swati"+out);

    }
    public static int binarySearch(int l, int h, int item, int arr[]) {
        if (l == h) {
            int arrVal = arr[l];
            if (item == arrVal) {
                return l;
            } else {
                return -1;
            }
        }
        int mid = l+ (h-l)/2;
        if (arr[mid] == item) {
            return mid;
        }
        if (arr[mid] > item) {
            h = mid - 1;
        } else {
            l = mid + 1;
        }
        return  binarySearch(l, h, item, arr);


    }
}
