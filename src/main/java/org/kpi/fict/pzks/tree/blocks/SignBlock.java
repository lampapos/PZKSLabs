package org.kpi.fict.pzks.tree.blocks;

import org.kpi.fict.pzks.automate.concreteAuto.arifm.dto.ResultElement;

public class SignBlock extends Block {
    public enum SignType {
        PLUS('+'), MINUS('-'), MUL('*'), DIV('/');
        
        private char signChar;
        
        private SignType(char signChar) {
            this.signChar = signChar;
        }
        
        @Override
        public String toString() {
            return "" + signChar;
        }
    }

    private SignType signType;
    
    public SignBlock(ResultElement sign) {
        char signChar = sign.getString().charAt(0);
        
        switch (signChar) {
        case '+': signType = SignType.PLUS;  break;
        case '-': signType = SignType.MINUS;   break;
        case '*': signType = SignType.MUL;   break;
        case '/': signType = SignType.DIV;   break;
        default:
            System.err.println("Error in sign type constructor.");
            break;
        }
    }
    
    public SignBlock(SignType signType) {
        this.signType = signType;
    }

    public BlockType getBlockType() {
        return BlockType.SIGN;
    }
    
    public SignType getSignType() {
        return signType;
    }
    
    public SignBlock getInvertedSign() {
        switch (signType) {
        case PLUS: return new SignBlock(SignType.MINUS);
        case MINUS: return new SignBlock(SignType.PLUS);
        case MUL: return new SignBlock(SignType.DIV);
        case DIV: return new SignBlock(SignType.MUL);
        default:
            return null;
        }
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return new SignBlock(signType);
    }
    
    @Override
    public String toString() {
        return signType.toString();
    }
}
