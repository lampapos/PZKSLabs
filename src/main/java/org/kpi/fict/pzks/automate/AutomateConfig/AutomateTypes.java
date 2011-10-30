package org.kpi.fict.pzks.automate.AutomateConfig;

import java.io.File;

import org.kpi.fict.pzks.automate.concreteAuto.arifm.ArifmActionImpl;

public enum AutomateTypes {
    ARIFM("config.xml", ArifmActionImpl.class);
    
    private File configFile;
    
    private Class<?> actionsImpl;
    
    private AutomateTypes(String pathToConfig, Class<?> actionsImpl) {
        configFile = new File(pathToConfig);
        this.actionsImpl = actionsImpl;
    }
    
    public File configFile() {
        return configFile;
    }
    
    public Class<?> actionsImpl() {
        return actionsImpl;
    }
}
