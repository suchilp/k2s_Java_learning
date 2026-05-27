package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Exception1 {
    public static void main(String[] args) {

        File f = new File("C://a.text");
        try {
            FileReader obj=   new FileReader(f);
        }


        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }


    }
}
