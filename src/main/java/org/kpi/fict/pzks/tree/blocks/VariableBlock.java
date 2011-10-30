package org.kpi.fict.pzks.tree.blocks;

import org.kpi.fict.pzks.automate.concreteAuto.arifm.dto.ResultElement;

public class VariableBlock extends Block {
    private String name;

    public VariableBlock(ResultElement elem) {
        String name = elem.getString();
        
        if (name.charAt(0) == '-') {
            inverted = true;
            name = name.substring(1);
        }
        
        this.name = name;
    }

    private VariableBlock(String name, boolean isInverted) {
        this.name = name;
        this.inverted = isInverted;
    }
    
    public BlockType getBlockType() {
        return BlockType.VARIABLE;
    }

    @Override
    public String toString() {
        return super.toString() + name;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return new VariableBlock(name, inverted);
    }
}
