package org.kpi.fict.pzks.automate.concreteAuto.arifm.dto;

public class ResultElement {
    public enum ResultElementType {
        OPEN_BRACKET, CLOSE_BRACKET, CONSTANT, VARIABLE, SIGN, INVERTOR
    }
    
    private ResultElementType type;
    
    private String string;

    public ResultElement(ResultElementType type, String string) {
        this.type = type;
        this.string = string;
    }

    public String getString() {
        return string;
    }
    
    public ResultElementType getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return string + "\t" + type;
    }
}
