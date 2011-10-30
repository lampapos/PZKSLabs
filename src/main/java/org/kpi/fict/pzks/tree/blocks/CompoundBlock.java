package org.kpi.fict.pzks.tree.blocks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.kpi.fict.pzks.tree.blocks.SignBlock.SignType;

public class CompoundBlock extends Block implements Iterable<Block> {
    public enum BlockSignType {
        /** Block contains only + and - signs. */
        PLUSMIN, 
        
        /** Block contains only * and / */
        MULDIV, 
        
        /** Block contains mix of *, /, +, - signs */
        MIXED
    }
    
    private ArrayList<Block> subBlocks; 
    
    public CompoundBlock() {
        subBlocks = new ArrayList<Block>();
    }
    
    public CompoundBlock(ArrayList<Block> subBlocks) {
        this.subBlocks = subBlocks;
    }

    public CompoundBlock(ArrayList<Block> subBlocks, boolean inverted) {
        this.subBlocks = subBlocks;
        this.inverted = inverted;
    }
    
    public CompoundBlock(Block [] blocks) {
        subBlocks = new ArrayList<Block>();
        for (int i = 0; i < blocks.length; i++) {
            subBlocks.add(blocks[i]);
        }
    }
    
    public void addBlock(Block block) {
        subBlocks.add(block);
    }
    
    public void setSubBlocks(ArrayList<Block> subBlocks) {
        this.subBlocks = subBlocks;
    }
    
    public BlockType getBlockType() {
        return BlockType.COMPOUND;
    }

    public Iterator<Block> iterator() {
        return subBlocks.iterator();
    }    
    
    public int size() {
        return subBlocks.size();
    }
    
    public Block get(int index) {
        return subBlocks.get(index);
    }
    
    public int indexOfSubblock(Block block) {
        return subBlocks.indexOf(block);
    }
    
    public void removeSubblock(int index) {
        subBlocks.remove(index);
    }
    
    public void addSubblocks(Collection<? extends Block> blocks) {
        subBlocks.addAll(blocks);
    }
    
    public BlockSignType getBlockSignType() {
        Set<SignBlock.SignType> blockSigns = new HashSet<SignBlock.SignType>();
        
        for (Block b : subBlocks) {
            if (b.isSign()) {
                blockSigns.add(((SignBlock) b).getSignType());
            }
        }
        
        if ( (blockSigns.contains(SignType.PLUS) || blockSigns.contains(SignType.MINUS)) &&
                (blockSigns.contains(SignType.MUL) || blockSigns.contains(SignType.DIV)) ) {
            return BlockSignType.MIXED;
        } else {
            if ( blockSigns.contains(SignType.PLUS) || blockSigns.contains(SignType.MINUS) )
                return BlockSignType.PLUSMIN;
            else 
                return BlockSignType.MULDIV;
        }
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("(");
        
        for (Block block : subBlocks) {
            builder.append(block.toString());
        }
        
        builder.append(")");
        
        return super.toString() + builder.toString();
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return new CompoundBlock(subBlocks, inverted);
    }
}
