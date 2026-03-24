package org.myString;

public class StringExample {

    public static void main(String[] args) {

        String s = "SUCHIL1250";  //lihcus
        String reverseStr = reverseString(s);
        System.out.println(reverseStr);
        checkVowelConsonants(s);

    }

    public static String reverseString(String s) {

        String reverse = "";
        for (int i = s.length() - 1; i >= 0; i--) {
            reverse = reverse + s.charAt(i);
        }
        return reverse;
    }

    public static void checkVowelConsonants(String s) {
        int vowel = 0;
        int consonants = 0;
        s=s.toLowerCase();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            System.out.println((int)c);
            if (c >= 'a' && c <= 'z') {
                if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
                    vowel++;
                } else {
                    consonants++;
                }
            }
        }
        System.out.println("No of Vowel is " + vowel);

        System.out.println("No of Consonant is " + consonants);

    }


}
