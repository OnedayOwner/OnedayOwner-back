package com.OnedayOwner.server.global.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {
    public static String streamToString(InputStreamReader inputStreamReader) {
        String str = "";
        BufferedReader br = new BufferedReader(inputStreamReader);
        try {
            int c;
            while ((c = br.read()) != -1) {
                str += (char) c;
            }
            inputStreamReader.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}
