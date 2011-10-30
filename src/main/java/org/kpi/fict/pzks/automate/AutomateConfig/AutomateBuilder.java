package org.kpi.fict.pzks.automate.AutomateConfig;

import java.util.ArrayList;
import java.util.HashMap;

import org.kpi.fict.pzks.automate.base.ActionsImplPool;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class AutomateBuilder {

    /**
     * Bean which contains automates config info. Used to read config from file.
     */
    @XStreamAlias("automate")
    private static class AutomateConfig {
        @XStreamAlias("actions")
        private ArrayList<Action> actions;        
        
        @XStreamAlias("charsets")
        private ArrayList<CharSet> charsets;
        
        @XStreamAlias("states")
        private ArrayList<State> states;
    }
    
    public static Automate buildAutomate(AutomateTypes autoType) {
        XStream stream = new XStream();
        stream.processAnnotations(AutomateConfig.class);
        
        AutomateConfig config = (AutomateConfig) stream.fromXML(autoType.configFile());
        
        // Initialize states
        for (State state : config.states) {
            state.init();
        }
        
        // Initialize actions
        HashMap<Integer, Action> actions = new HashMap<Integer, Action>();
        ActionsImplPool actImpl = null;
        
        try {
            actImpl = (ActionsImplPool) autoType.actionsImpl().newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        for (Action action : config.actions) {
            actions.put(action.getNum(), action);
            action.setActionImplPool(actImpl);
        }
        
        // Initialize charSorter
        CharSorter charSorter = new CharSorter(config.charsets);
        
        return new Automate(actions, charSorter, config.states, actImpl);
    }
}