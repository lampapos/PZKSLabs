package org.kpi.fict.pzks.gui;

public class Node {
    private Node left;
    private Node right;
    
    private String label;

    private boolean hasChildren;
    
    public Node(Node left, Node right, String label) {
        this.left = left;
        this.right = right;
        this.label = label;
        hasChildren = true;
    }

    public Node(String label) {
        this.label = label;
        hasChildren = false;
    }
    
    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public String getLabel() {
        return label;
    }  
    
    public boolean hasChildren() {
        return hasChildren;
    }
}
