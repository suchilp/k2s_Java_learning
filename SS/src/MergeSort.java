public class MergeSort {
    public static void main (String[]args){
        int[]arr={9,20,4,10,50,49,2,8,43,11};
        int l=0;
        int r=arr.length-1;
        mergeSort(arr,l,r);
        for(int num:arr){
            System.out.println(num+" ");
        }

    }
    public static void mergeSort(int arr[],int l,int r){
        if (l<r){
           int mid=l+(r-l)/2;
           mergeSort(arr,l,mid);
           mergeSort(arr,mid+1,r);
           merge(arr,l,mid,r);
        }
    }

}
   public static void merge(int[]arr,int l,int mid,int r){
        int m=mid-l+1;
        int n=r-mid;
        int L []=new int[m];
        int R[]=new int [n];
        for(int i=0;i<m;i++){
            L[i]=arr[l+1];
        }
        for(int j=0;j<n;j++){
            R[j]=arr[mid+1+j];
        }
        int i=0;
        int j=0;
        int k=l;
        while (i<m && j<n){
            if(L[i]<=R[j]){
                arr[k]=L[i];
                i++;
                k++;
            }else{
               arr[k]=R[j];
               i++;
               k++;
            }
        }
        while(i<m) {
            arr[k] = L[i];
            i++;
            k++;
        }
        while (j<n){
            arr[k]=R[j];
            j++;
            k++;
        }
        }

void main() {
}



