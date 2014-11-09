package com.brainwaves.cybergods.nbanking;


/**
 * Created by utkarsh on 9/11/14.
 */


import java.io.*;
import java.util.Scanner;
/**
 * Created by vb-deb on 9/11/14.
 */
class FileIO {


    public static String readFrom() {

        String s=null;
        File file = new File("/sdcard/output");
        try {
            s = new Scanner(file).useDelimiter("\\Z").next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
    public static void writeTo(String s)
    {

        try {
            Writer w = new FileWriter("/sdcard/input");
            w.write(s+"\n");
            w.write("hellohelloasdasdkj\n");
            w.close()   ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    }

