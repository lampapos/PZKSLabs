package org.kpi.fict.pzks.automate.AutomateConfig;

import java.util.ArrayList;
import java.util.HashMap;

public class CharSorter {
    public static final String END = "END";
    
    private HashMap<Character, String> encodeTable;
    
    protected CharSorter(ArrayList<CharSet> charsets) {
        encodeTable = new HashMap<Character, String>();
        
        for (CharSet set : charsets) {
           registrateCharType(set.getCharsTypeName(), set.getChars()); 
        }
    }
    
    private void registrateCharType(String charType, String chars) {
        for (int i = 0; i < chars.length(); i++) {
            encodeTable.put(chars.charAt(i), charType);
        }
    }
    
    public String sortChar(int ch) {
        if (ch == -1) return END;
        return encodeTable.get((char) ch);
    }
}
