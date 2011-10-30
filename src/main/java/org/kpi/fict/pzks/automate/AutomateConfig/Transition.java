package org.kpi.fict.pzks.automate.AutomateConfig;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("transition")
public class Transition {
    @XStreamAsAttribute
    private String charType;
    
    @XStreamAsAttribute
    private ActionType actionType;
    
    @XStreamAsAttribute
    @XStreamAlias("num")
    private int actionNum;
    
    @XStreamAsAttribute
    @XStreamAlias("next")
    private int nextState;
    
    @XStreamAsAttribute
    private String desc;
    
    public enum ActionType {
        ACTION, ERROR, EXIT
    }
        
    public ActionType getActionType() {
        return actionType;
    }

    public String getCharType() {
        return charType;
    }

    public int getActionNum() {
        return actionNum;
    }

    public int getNextState() {
        return nextState;
    }
    
    public String getDescription() {
        return desc;
    }
    
    @Override
    public String toString() {
        switch (actionType) {
        case ACTION:
        return "Transition to " + nextState + " actionType " + actionType + 
                " actionNum/errNum " + actionNum + " chars " + charType;
        case ERROR:
            return "Transition to ERROR " + desc;
        case EXIT:
            return "Transition to EXIT " + " actionNum " + actionNum;
        default:
            return "";
        }
    }
}
