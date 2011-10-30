package org.kpi.fict.pzks.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.kpi.fict.pzks.automate.AutomateConfig.Automate;
import org.kpi.fict.pzks.automate.AutomateConfig.AutomateBuilder;
import org.kpi.fict.pzks.automate.AutomateConfig.AutomateTypes;
import org.kpi.fict.pzks.automate.base.ParcingString;
import org.kpi.fict.pzks.automate.concreteAuto.arifm.dto.ResultStructure;
import org.kpi.fict.pzks.automate.exceptions.ParceException;
import org.kpi.fict.pzks.tree.TreeBuilder;
import org.kpi.fict.pzks.tree.blocks.Block;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

    private JPanel contentPane;
    private JTextField expressionField;

    private TreePanel treePanel;
    
    private JTextArea logConsole;
    
    private void buttonPressed() {
        ParcingString string = new ParcingString(expressionField.getText());
        
        Automate auto = AutomateBuilder.buildAutomate(AutomateTypes.ARIFM);
        ResultStructure res = null;
        try {
            res = auto.parce(string);
            logConsole.append("Parsed without errors\n");
            
            TreeBuilder builder = TreeBuilder.getInstance();
            ArrayList<Block> resBlocks = builder.buildTree(res);

            
            logConsole.append("\n\nVariants:\n");
            int curPos = 0;
            int minPos = 0;
            int minDeep = 1000;
            for (Block b : resBlocks) {
                int deep = TreeBuilder.treeDeep(builder.makeBinaryBlocks(b));
                if (deep < minDeep) {
                    minPos = curPos;
                    minDeep = deep;
                }
                
                logConsole.append(b.toString() + "\t Deep = " + deep + "\n");
                curPos++;
            }
            
            Block binResBlock = builder.makeBinaryBlocks(resBlocks.get(minPos));
            logConsole.append("\nResult expression: " + binResBlock + "\n");

            GraphBuilder grB = GraphBuilder.getInstance();
            Node node = grB.buildGraph(binResBlock);
            treePanel.setTree(node);
        } catch (ParceException e) {
            logConsole.append(e.getMessage() + "\n");
        }
    }
    
    /**
     * Create the frame.
     */
    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 600, 380);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());
        
        // Adding of tree panel
        treePanel = new TreePanel();
        JScrollPane treeScrollPane = new JScrollPane(treePanel);
        
        contentPane.add(treeScrollPane, BorderLayout.CENTER);
        
        AdjustmentListener listener = new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                treePanel.repaint();
            }
        };
        
        treeScrollPane.getHorizontalScrollBar().addAdjustmentListener(listener);
        treeScrollPane.getVerticalScrollBar().addAdjustmentListener(listener);
        
        // Adding of expression (top) panel
        JPanel expressionPanel = new JPanel();
        contentPane.add(expressionPanel, BorderLayout.NORTH);
        expressionPanel.setLayout(new BorderLayout(10, 0));
        
        expressionField = new JTextField();
        expressionPanel.add(expressionField, BorderLayout.CENTER);
        
        JButton btnProcess = new JButton("Process");
        
        btnProcess.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonPressed();
            }
        });
        
        expressionPanel.add(btnProcess, BorderLayout.EAST);
        
        // Adding of console (bottom panel)
        logConsole = new JTextArea(7, 0);
        JScrollPane consoleScrollPane = new JScrollPane(logConsole);
        contentPane.add(consoleScrollPane, BorderLayout.SOUTH);
        
        ParcingString string = new ParcingString(new File("arifm.txt"));
        expressionField.setText(string.toString());
    }

}
