package org.kpi.fict.pzks;

import java.awt.EventQueue;

import org.kpi.fict.pzks.gui.MainFrame;

public class Start {

    /**
     * Entry point into 
     * 
     * @param args
     */
    public static void main(String[] args) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            MainFrame frame = new MainFrame();
                            frame.setVisible(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
