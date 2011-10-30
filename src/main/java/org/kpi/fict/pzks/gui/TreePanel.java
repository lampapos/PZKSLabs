package org.kpi.fict.pzks.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TreePanel extends JPanel {
    public final static int VERTICAL_INDENT = 70;
    public final static int MIN_HORISONTAL_INDENT = 35;
    public final static int NODE_SIZE = 30;
    private int height, width, treeDeep;
    private Node headNode;
    
    public TreePanel() { 
        setOpaque(false);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        if (headNode != null) {
            drawNode(headNode, width / 2, 0, width, g);
        }
    }
    
    public void setTree(Node node) {
        headNode = node;
        
        treeDeep = measureTreeDeep(node);
        
        System.out.println("Tree deep = " + treeDeep);
        
        // Calculate height
        height = treeDeep * VERTICAL_INDENT;
        
        // Calculate weight
        int weightInNodes = 1;
        for (int i = 0; i < treeDeep; i++) {
            weightInNodes <<= 1;
        }
        
        width = weightInNodes * MIN_HORISONTAL_INDENT + NODE_SIZE;
        height = treeDeep * VERTICAL_INDENT + NODE_SIZE;
        
        setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        //repaint();
    }
    
    private void drawNode(Node node, int Xcoord, int level, int width, Graphics g) {
        if (node.hasChildren()) {
            int leftX = Xcoord - width / 4;
            int rightX = Xcoord + width / 4;
            int childY = (level + 1) * VERTICAL_INDENT + NODE_SIZE / 2;
            
            g.setColor(Color.BLACK);
            g.drawLine(Xcoord, level * VERTICAL_INDENT + NODE_SIZE / 2, leftX , childY);
            g.drawLine(Xcoord, level * VERTICAL_INDENT + NODE_SIZE / 2, rightX , childY);
            
            drawNode(node.getLeft(),  leftX,  level + 1, width / 2, g);
            drawNode(node.getRight(), rightX, level + 1, width / 2, g);
        }
        
        int leftUpCornerX = Xcoord - NODE_SIZE / 2;
        int leftUpCornerY = level * VERTICAL_INDENT;
        
        g.setColor(Color.WHITE);
        g.fillOval(leftUpCornerX, leftUpCornerY, NODE_SIZE, NODE_SIZE);
        g.setColor(Color.BLACK);
        g.drawOval(leftUpCornerX, leftUpCornerY, NODE_SIZE, NODE_SIZE);
        g.drawString(node.getLabel(), leftUpCornerX + NODE_SIZE / 3, leftUpCornerY + NODE_SIZE / 3 * 2);
    }
    
    private int measureTreeDeep(Node node) {
        if (!node.hasChildren()) {
            return 0;
        } else {
            int leftDeep = measureTreeDeep(node.getLeft());
            int rightDeep = measureTreeDeep(node.getRight());
            
            int max = (leftDeep > rightDeep) ? leftDeep : rightDeep;
            
            return max + 1;
        }
    }
}
