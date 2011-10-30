package org.kpi.fict.pzks.automate.concreteAuto.arifm.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * Simple wrapper for ArrayList&lt;ResultElement&gt;.
 *
 * @author Pustovit Michael, pustovitm@gmail.com
 */
public class ResultStructure implements Iterable<ResultElement> {
    ArrayList<ResultElement> struct;
    
    public ResultStructure() {
        struct = new ArrayList<ResultElement>();
    }
    
    public void addElement(ResultElement element) {
        struct.add(element);
    }
    
    public void addElements(ArrayList<ResultElement> elements) {
        struct.addAll(elements);
    }
    
    public ResultElement get(int index) {
        return struct.get(index);
    }
    
    public ResultElement firstElement() {
        return struct.get(0);
    }
    
    public ResultElement lastElement() {
        return struct.get(struct.size() - 1);
    }
    
    public int size() {
        return struct.size();
    }
    
    public void add(ResultElement e) {
        struct.add(e);
    }
    
    public void add(int index, ResultElement e) {
        struct.add(index, e);
    }
    
    public ResultElement remove(int index) {
        return struct.remove(index);
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        for (ResultElement elem : struct) {
            builder.append(elem + "\n");
        }
        return builder.toString();
    }

    public Iterator<ResultElement> iterator() {
        return struct.iterator();
    }

    public ListIterator<ResultElement> listIterator() {
        return struct.listIterator();
    }
}
