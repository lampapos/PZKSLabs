package org.kpi.fict.pzks.tree.blocks;

import org.kpi.fict.pzks.automate.concreteAuto.arifm.dto.ResultElement;

public class ConstantBlock extends Block {
    private double value;

    public ConstantBlock(ResultElement elem) {
        String str = elem.getString();
        
        if (str.charAt(0) == '-') {
            inverted = true;
            str = str.substring(1);
        }
        
        value = Double.valueOf(str);
    }

    public ConstantBlock(double value, boolean inverted) {
        this.value = value;
        this.inverted = inverted;
    }
    
    public BlockType getBlockType() {
        return BlockType.CONSTANT;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return super.toString() + Double.toString(value);
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return new ConstantBlock(value, inverted);
    }
}
