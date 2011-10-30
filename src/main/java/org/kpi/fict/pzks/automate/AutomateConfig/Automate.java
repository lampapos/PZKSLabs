package org.kpi.fict.pzks.automate.AutomateConfig;

import java.util.ArrayList;
import java.util.HashMap;

import org.kpi.fict.pzks.automate.base.ActionsImplPool;
import org.kpi.fict.pzks.automate.base.ParcingString;
import org.kpi.fict.pzks.automate.concreteAuto.arifm.dto.ResultStructure;
import org.kpi.fict.pzks.automate.exceptions.ParceException;

public class Automate {
    private HashMap<Integer, Action> actions;
    private CharSorter charSorter;
    private ArrayList<State> states;
    private ActionsImplPool impl;
    
    public Automate(
            HashMap<Integer, Action> actions,
            CharSorter charSorter,
            ArrayList<State> states, 
            ActionsImplPool impl) 
    {
        this.actions = actions;
        this.charSorter = charSorter;
        this.states = states;
        this.impl = impl;
    }
    
    public ResultStructure parce(ParcingString in) throws ParceException {
        int nextState = 0;
        int curChar = 0;
        State curState;
        boolean exit = false;
        
        while (!exit) {
            curState = states.get(nextState);
            
            curChar = in.readChar();

            System.out.println(curState + " SYMBOL " + (char) curChar);
            
            String charType = charSorter.sortChar(curChar);
            Transition trans = curState.getTransition(charType);
            if (trans == null) {
                if (curChar == ParcingString.END_CHAR) {
                    throw new ParceException("Unexpected end");
                } else {
                    throw new ParceException("Illegal symbol at position " + (in.getPos() - 1));
                }
            }
            
            switch(trans.getActionType()) {
                case ACTION:
                    actions.get(trans.getActionNum()).doAction(curChar, in.getPos());
                    nextState = trans.getNextState();
                break;
                
                case ERROR:
                    throw new ParceException(
                            "Error at position: " + in.getPos() + "  "  
                                    + trans.getDescription());
                
                case EXIT:
                    actions.get(trans.getActionNum()).doAction(curChar, in.getPos());
                    exit = true;
                    break;
                
                default:
                    System.err.println("Illegal transition type");
                    break;
            }
        }
        
        return impl.getResult();
    }
}
