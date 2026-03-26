public class StringExample {
    public static void main(String[]args){
        String s="Swati123Samaira";
        String reverseStr = reverseString(s);
        System.out.println(reverseStr);
    }
    public static String reverseString(String s){
      String reverse=" ";
      for(int i=s.length()-1;i>=0;i--){
          reverse=reverse+s.charAt(i);
      }
      return reverse;
    }
}
