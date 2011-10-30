package org.kpi.fict.pzks.automate.AutomateConfig;

import org.kpi.fict.pzks.automate.base.ActionsImplPool;
import org.kpi.fict.pzks.automate.exceptions.ParceException;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("action")
public class Action {
    /** Number of action. */
    @XStreamAsAttribute
    int num;

    /** Action description. */
    @XStreamAsAttribute
    String desc;

    private ActionsImplPool implPool;
    
    protected void setActionImplPool(ActionsImplPool implPool) {
        this.implPool = implPool;
    }
    
    public void doAction(int curChar, int curPosition) throws ParceException {
        implPool.getAction(num).doAction(curChar, curPosition);
    }
    
    public int getNum() {
        return num;
    }

    public String getDesc() {
        return desc;
    }
    
    @Override
    public String toString() {
        return "Action " + num + " " + desc;
    }
}