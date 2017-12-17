package com.example.jutil.utils;

import java.util.Scanner;

public class ScannerUtil {
    private static Scanner sScanner = new Scanner(System.in);

    public static String input(String hint){
        System.out.print(hint+": ");
        return sScanner.nextLine();
    }
}