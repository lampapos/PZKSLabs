package org.kpi.fict.pzks.automate.base;

import java.util.HashMap;

import org.kpi.fict.pzks.automate.concreteAuto.arifm.dto.ResultStructure;
import org.kpi.fict.pzks.automate.exceptions.ParceException;

/**
 * Implementation of actions.
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public abstract class ActionsImplPool {
    protected ResultStructure resStruct;
    
    /**
     * Implementation of one action.
     */
    public interface ActionImpl {
        public void doAction(int curChar, int curPosition) throws ParceException;
    }
    
    private HashMap<Integer, ActionImpl> actions;
    
    public ActionsImplPool() {
        actions = new HashMap<Integer, ActionsImplPool.ActionImpl>();
        resStruct = new ResultStructure();
    }
    
    protected void addAction(int num, ActionImpl action) {
        actions.put(num, action);
    }
    
    public ActionImpl getAction(int actionNumber) {
        return actions.get(actionNumber);
    }
    
    public ResultStructure getResult() {
        return resStruct;
    }
}
