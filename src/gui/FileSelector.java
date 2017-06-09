/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Czapol
 */
public class FileSelector extends javax.swing.JFrame
{

    private static File lastSelected;
    private static int lastX = -1;
    private static int lastY = -1;

    private static int lastX2 = -1;
    private static int lastY2 = -1;

    public enum SelectorType
    {

        OPEN_MAIN, OPEN_CODED, OPEN_FILE, SAVE_CODED
    };

    SelectorType type;

    public FileSelector(SelectorType type)
    {
        initComponents();
        this.type = type;

        switch (type)
        {
            case OPEN_CODED:
            case OPEN_MAIN:
                jFileChooser1.setDialogType(0);
                jFileChooser1.setFileFilter(new FileNameExtensionFilter("Graphics Files", "png", "bmp", "jpg", "tga"));
                break;
            case OPEN_FILE:
                jFileChooser1.setDialogType(0);
                break;
            case SAVE_CODED:
                jFileChooser1.setDialogType(1);
                jFileChooser1.setFileFilter(new FileNameExtensionFilter("Graphics Files", "png", "bmp", "jpg", "tga"));
        }
        if (lastSelected != null)
        {
            jFileChooser1.setCurrentDirectory(lastSelected);
        }
        if (lastX > 0)
        {
            this.setSize(lastX, lastY);
           
            this.setLocation(lastX2, lastY2);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jFileChooser1 = new javax.swing.JFileChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(600, 430));
        setPreferredSize(new java.awt.Dimension(600, 430));
        setType(java.awt.Window.Type.UTILITY);

        jFileChooser1.setCurrentDirectory(new java.io.File("C:\\"));
            jFileChooser1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            jFileChooser1.setMinimumSize(new java.awt.Dimension(564, 385));
            jFileChooser1.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    jFileChooser1ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jFileChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jFileChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jFileChooser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFileChooser1ActionPerformed
        if (evt.getActionCommand().equals("ApproveSelection"))
        {
            MainWindow.SetPath(type, jFileChooser1.getSelectedFile());
            this.dispose();
        }
        else if (evt.getActionCommand().equals("CancelSelection"))
        {
            this.dispose();
        }
        lastSelected = jFileChooser1.getCurrentDirectory();
        lastX = this.getX();
        lastY = this.getY();
        lastX2 = this.getLocation().x;
        lastY2 = this.getLocation().y;
    }//GEN-LAST:event_jFileChooser1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser jFileChooser1;
    // End of variables declaration//GEN-END:variables
}
