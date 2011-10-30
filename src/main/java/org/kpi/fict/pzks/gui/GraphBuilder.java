package org.kpi.fict.pzks.gui;

import org.kpi.fict.pzks.tree.blocks.Block;
import org.kpi.fict.pzks.tree.blocks.Block.BlockType;
import org.kpi.fict.pzks.tree.blocks.CompoundBlock;

public class GraphBuilder {
    private static GraphBuilder singletonInstance;
    
    private GraphBuilder() { }
    
    public static GraphBuilder getInstance() {
        return (singletonInstance == null) ? (singletonInstance = new GraphBuilder()) : singletonInstance;
    }
    
    public Node buildGraph(Block block) {
        if (block.getBlockType() != BlockType.COMPOUND) {
            return new Node(block.toString());
        } else {
            CompoundBlock compBlock = (CompoundBlock) block;
            Node left = buildGraph(compBlock.get(0));
            Node right = buildGraph(compBlock.get(2));
            return new Node(left, right, compBlock.get(1).toString());
        }
    }
}
