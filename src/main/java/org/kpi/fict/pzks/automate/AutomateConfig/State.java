package org.kpi.fict.pzks.automate.AutomateConfig;

import java.util.ArrayList;
import java.util.HashMap;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("state")
public class State {
    public static final int ILLEGAL_TRANSITION = -1;
    
    @XStreamAsAttribute
    private int num;
    
    @XStreamAsAttribute
    private String desc;
    
    /** Transitions array (is used in xml-file parsing process). */
    @XStreamAlias("transitions")
    private ArrayList<Transition> transArray;
    
    /** Map which links correspond CharSets and Actions (or errors). */
    private HashMap<String, Transition> transitions;
    
    /** Initialization of transactions map. */
    protected void init() {
        transitions = new HashMap<String, Transition>();
        if (transArray != null) {
            for (Transition trans : transArray) {
                transitions.put(trans.getCharType(), trans);
            }
        }
    }

    public int nextState(String charSet) {
        return transitions.containsKey(charSet) ? transitions.get(charSet).getNextState() : ILLEGAL_TRANSITION; 
    }
    
    public int getNum() {
        return num;
    }

    public String getDesc() {
        return desc;
    }

    public Transition getTransition(String charType) {
        return transitions.get(charType);
    }
    
    @Override
    public String toString() {
        return "State " + num + " Desc: " + desc;
    }
}
