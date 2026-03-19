public class BSItt {
    public static void main(String[]args){
        int arr[]={10,20,24,25,30,32,35,40,41,46,78,98};
        int out=BSItt(arr,78);
        System.out.println(out);

    }
    public static int BSItt(int arr[],int key) {
        int l = 0;
        int h = arr.length - 1;
        while (l <= h) {
            int mid = l + (h - l) / 2;
            if (arr[mid] == key) {
                return mid;
            }
            if (arr[mid] < key) {
                l = mid + 1;
            } else {
                h = mid - 1;
            }
        }
            return -1;
        }

    }
