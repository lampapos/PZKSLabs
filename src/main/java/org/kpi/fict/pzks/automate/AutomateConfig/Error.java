package org.kpi.fict.pzks.automate.AutomateConfig;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("error")
public class Error {
    /** Number of action. */
    @XStreamAsAttribute
    int num;

    /** Action description. */
    @XStreamAsAttribute
    String desc;

    public int getNum() {
        return num;
    }

    public String getDesc() {
        return desc;
    }        
    
    @Override
    public String toString() {
        return "Error " +  num + " " + desc;
    }
}
