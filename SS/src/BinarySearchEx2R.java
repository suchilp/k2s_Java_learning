public class BinarySearchEx2R {
    public static void main (String[]args){
        int arr[]={2,4,5,6,7,8,9,10,11,23,24,37,38,48,49,52,53};
        int out=binarySearch(arr,0,arr.length-1,49);
        System.out.println(out);
    }
    public static int binarySearch(int arr[],int l,int h,int key){
        if(l>h){
            return -1;
        }
        int mid=(l+h)/2;
        if(arr[mid]==key){
            return mid;

        }
        if(arr[mid]<key){
            return binarySearch(arr,l,mid+1,key);
        }
        else{
            return binarySearch(arr,mid-1,h,key);
        }

    }
}
