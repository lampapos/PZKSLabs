package org.kpi.fict.pzks.automate.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ParcingString {
    public static final int END_CHAR = -1;
    
    private int curPos;
    private String string;
    
    public ParcingString(File source) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(source));
        } catch (FileNotFoundException e) {
            System.err.println("Source file not found");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        StringBuilder strBuilder = new StringBuilder();
        int ch;
        try {
            while ((ch = reader.read()) >= 0) {
                strBuilder.append((char) ch);
            }
        } catch (IOException e) {
            System.err.println("Problems while reading source file");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        string = strBuilder.toString().trim();
    }
    
    public ParcingString(String string) {
        this.string = string.trim();
    }
    
    public int readChar() {
        return string.length() > curPos ? string.charAt(curPos++) : END_CHAR; 
    }
    public int getPos() {
        return curPos;
    }
    
    @Override
    public String toString() {
        return string;
    }
}
