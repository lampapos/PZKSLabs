package org.kpi.fict.pzks.automate;

import java.io.File;

import org.kpi.fict.pzks.automate.AutomateConfig.Automate;
import org.kpi.fict.pzks.automate.AutomateConfig.AutomateBuilder;
import org.kpi.fict.pzks.automate.AutomateConfig.AutomateTypes;
import org.kpi.fict.pzks.automate.base.ParcingString;
import org.kpi.fict.pzks.automate.concreteAuto.arifm.dto.ResultStructure;
import org.kpi.fict.pzks.automate.exceptions.ParceException;

public class StartParsing {

    /**
     * Entry point. Start parsing of input file by automate.
     * 
     * @param args console params (aren't used)
     */
    public static void main(String[] args) {
        ParcingString string = new ParcingString(new File("arifm.txt"));
        
        Automate auto = AutomateBuilder.buildAutomate(AutomateTypes.ARIFM);
        ResultStructure res = null;
        System.out.println("========TRACE========");
        try {
            res = auto.parce(string);
        } catch (ParceException e) {
            System.err.println(e.getMessage());
        }
        
        System.out.println("\n\n\n========RESULT========\n" + res);
    }

}
