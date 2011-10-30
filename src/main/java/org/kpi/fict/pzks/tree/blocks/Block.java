package org.kpi.fict.pzks.tree.blocks;

import org.kpi.fict.pzks.tree.blocks.SignBlock.SignType;

public abstract class Block implements Cloneable {
    public abstract BlockType getBlockType();
    
    protected boolean inverted = false; // if block have sign - before bracket - block inverted
    
    public enum BlockType {
        /** Block which contains several sub blocks. */
        COMPOUND, 
        
        /** Block which contains constant. */
        CONSTANT,
        
        /** Block which contains variable. */
        VARIABLE, 
        
        /** Block which contains sign. */
        SIGN;
    }
    
    public boolean isInverted() {
        return inverted;
    }
    
    public void invert() {
        inverted = !inverted;
    }
    
    @Override
    public String toString() {
        return inverted?"-":"";
    }
    
    // Test for sign type blocks
    public boolean isPlus() { 
        return isSign() && ((SignBlock) this).getSignType() == SignType.PLUS;
    }
    
    public boolean isMinus() { 
        return isSign() && ((SignBlock) this).getSignType() == SignType.MINUS;
    }
    
    public boolean isMultiplication() { 
        return isSign() && ((SignBlock) this).getSignType() == SignType.MUL;
    }
    
    public boolean isDivision() { 
        return isSign() && ((SignBlock) this).getSignType() == SignType.DIV;
    }
    
    // Test for other types of blocks
    public boolean isCompound() { 
        return getBlockType() == BlockType.COMPOUND;
    }
    
    public boolean isConstant() { 
        return getBlockType() == BlockType.CONSTANT;
    }
    
    public boolean isVariable() { 
        return getBlockType() == BlockType.VARIABLE;
    }
    
    public boolean isSign() { 
        return getBlockType() == BlockType.SIGN;
    }
    
    @Override
    public abstract Object clone() throws CloneNotSupportedException;

//    // Cast to other types
//    public CompoundBlock castToCompound() {
//        return (CompoundBlock) this;
//    }
//    
//    public ConstantBlock castToConstant() {
//        return (ConstantBlock) this;
//    }
//    
//    public VariableBlock castToVariable() {
//        return (VariableBlock) this;
//    }
//    
//    public SignBlock castToSign() {
//        return (SignBlock) this;
//    }
}