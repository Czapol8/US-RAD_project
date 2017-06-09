/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;
import algorithms.Algorithm_lsb;
import algorithms.Algorithm_lsb_packing;
import algorithms.Algorithm_lsb_secret;
import src.Algorytm;
import src.AlgorytmRunner;
import src.Refresher;
import src.Util;

/**
 *
 * @author Czapol
 */
public class MainWindow extends javax.swing.JFrame
{

    private final Algorytm[] algorithms =
    {
        new Algorithm_lsb(),
        new Algorithm_lsb_secret(),
        new Algorithm_lsb_packing(false),
        new Algorithm_lsb_packing(true)
    };
    private int selectedAlgo;

    private boolean mode;
    private Refresher refresher;
    private AlgorytmRunner runner;

    private int maxCodedSize;
    private int toCodeSize;

    private static MainWindow main;
    BufferedImage mainImage;

    public static void SetPath(FileSelector.SelectorType type, File path)
    {

        if (path != null)
            if (type == FileSelector.SelectorType.OPEN_MAIN)
            {
                main.text_path_source.setText(path.getAbsolutePath());
                main.mainImage = Util.GetImage(path);
                main.updateMainImageInfo();
            }
            else if (type == FileSelector.SelectorType.SAVE_CODED)
            {
                main.text_pathOutput.setText(path.getAbsolutePath());
                main.CheckCanProcess();
            }
            else if (type == FileSelector.SelectorType.OPEN_FILE)
            {
                main.text_pathFile.setText(path.getAbsolutePath());
                main.updateCodeInfo();
            }
            else if (type == FileSelector.SelectorType.OPEN_CODED)
            {
                main.text_pathCodeImage.setText(path.getAbsolutePath());
                main.updateDecodeInfo();
                main.CheckCanProcess();
            }
    }

    private void updateMainImageInfo()
    {
        if (mainImage != null)
        {
            label_selectedImageView.setIcon(new ImageIcon(mainImage.getScaledInstance(100, 100, 2)));
            textLabel_MainImageX.setText(mainImage.getWidth() + "");
            textLabel_MainImageY.setText(mainImage.getHeight() + "");
            textLabel_MainImagePix.setText(mainImage.getWidth() * mainImage.getHeight() + "");
            textLabel_MainImageSize.setText(String.format("%.2f KB", (float) new File(main.text_path_source.getText()).length() / 1024));

        }
        else
        {
            label_selectedImageView.setIcon(null);
            textLabel_MainImageX.setText("");
            textLabel_MainImageY.setText("");
            textLabel_MainImagePix.setText("");
            textLabel_MainImageSize.setText("");
            maxCodedSize = 0;
            textLabel_algoMaxSize.setText("0 KB");
        }
        updateCodeInfo();
    }

    private void updateCodeInfo()
    {
        File f = new File(text_pathFile.getText());
        if (mainImage != null)
        {
            maxCodedSize = algorithms[selectedAlgo].GetMaxData(mainImage, new File(text_pathFile.getText()));
            textLabel_algoMaxSize.setText(String.format("%.2f KB", (float) maxCodedSize / 1024));
        }
        if (f.exists())
        {
            long check = f.length();
            if (check > Integer.MAX_VALUE)
            {
                toCodeSize = -1;
                textLabel_toCodeSize.setText("Plik jest zbyt duży!");
                CheckCanProcess();
                return;
            }
            else
            {
                toCodeSize = (int) check;
                textLabel_toCodeSize.setText(String.format("%.2f KB", (float) toCodeSize / 1024));
            }
            if (maxCodedSize > toCodeSize)
            {
                textLabel_toCodeSize.setForeground(Color.black);

                progresBar_fileUsage.setValue((int) (((float) toCodeSize / (float) maxCodedSize) * 100));
                if (toCodeSize > maxCodedSize * 0.75)
                    progresBar_fileUsage.setBackground(Color.orange);
                else
                    progresBar_fileUsage.setBackground(Color.green);
            }
            else
            {
                textLabel_toCodeSize.setForeground(Color.red);
                progresBar_fileUsage.setValue((int) (((float) toCodeSize / (float) maxCodedSize) * 100));
                progresBar_fileUsage.setBackground(Color.red);
            }

        }//TODO

        CheckCanProcess();
    }

    private void updateDecodeInfo()
    {
        File f = new File(text_pathCodeImage.getText());
        if (f.exists())
        {
            BufferedImage bi = Util.GetImage(f);
            if (bi != null)
            {
                textLabel_xDecode.setText(bi.getWidth() + "");
                textLabel_yDecode.setText(bi.getHeight() + "");

                if (mainImage != null)
                    if (mainImage.getWidth() == bi.getWidth() && mainImage.getHeight() == bi.getHeight())
                    {
                        textLabel_xDecode.setForeground(Color.GREEN);
                        textLabel_yDecode.setForeground(Color.GREEN);
                        button_process.setEnabled(true);
                    }
                    else
                    {
                        textLabel_xDecode.setForeground(Color.RED);
                        textLabel_yDecode.setForeground(Color.RED);
                        button_process.setEnabled(false);
                    }
            }

        }else
             button_process.setEnabled(false);
    }

    private void CheckCanProcess()
    {
        if (mode)//deszyfrowanie
            updateDecodeInfo();
        else
        {//szyfrowanie
            if (mainImage != null && new File(main.text_pathFile.getText()).exists() && main.text_pathOutput.getText().length() > 2)
                if (maxCodedSize > toCodeSize)
                {
                    button_process.setEnabled(true);
                    return;
                }
            button_process.setEnabled(false);
        }
    }

    public MainWindow()
    {
        initComponents();

        decodingPanel.hide();
        combo_SelectedCodeAlgo.removeAllItems();
        combo_decodeAlgo.removeAllItems();
        for (Algorytm a : algorithms)
        {
            combo_SelectedCodeAlgo.addItem(a.toString());
            combo_decodeAlgo.addItem(a.toString());
        }
        refresher = new Refresher(progressBar, textLabel_processDescrypt);
        refresher.SetAlgo(algorithms[0]);
        runner = new AlgorytmRunner();
        runner.SetAlgorytm(algorithms[0]);
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

        basicPanel = new javax.swing.JPanel();
        button_selectMainImage = new javax.swing.JButton();
        text_path_source = new javax.swing.JTextField();
        label_selectedImageView = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        textLabel_MainImageX = new javax.swing.JLabel();
        textLabel_MainImageY = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        textLabel_MainImagePix = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        textLabel_MainImageSize = new javax.swing.JLabel();
        button_changeMode = new javax.swing.JButton();
        codingPanel = new javax.swing.JPanel();
        text_pathFile = new javax.swing.JTextField();
        button_selectFileToCode = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        textLabel_toCodeSize = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        combo_SelectedCodeAlgo = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        textLabel_algoMaxSize = new javax.swing.JLabel();
        text_pathOutput = new javax.swing.JTextField();
        button_selectOutputFile = new javax.swing.JButton();
        progresBar_fileUsage = new javax.swing.JProgressBar();
        outputPanel = new javax.swing.JPanel();
        button_process = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        textLabel_processDescrypt = new javax.swing.JLabel();
        decodingPanel = new javax.swing.JPanel();
        text_pathCodeImage = new javax.swing.JTextField();
        button_selectImageToUncode = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        combo_decodeAlgo = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        textLabel_xDecode = new javax.swing.JLabel();
        textLabel_yDecode = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Steganograf");
        setPreferredSize(new java.awt.Dimension(593, 610));
        setResizable(false);

        basicPanel.setBackground(new java.awt.Color(204, 204, 255));

        button_selectMainImage.setText("wybierz obraz");
        button_selectMainImage.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                button_selectMainImageActionPerformed(evt);
            }
        });

        text_path_source.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                text_path_sourceActionPerformed(evt);
            }
        });

        label_selectedImageView.setBackground(new java.awt.Color(102, 102, 102));
        label_selectedImageView.setForeground(new java.awt.Color(102, 102, 102));
        label_selectedImageView.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setText("Szerokość:");

        jLabel6.setText("Wysokość:");

        textLabel_MainImageX.setText(" ");

        textLabel_MainImageY.setText(" ");

        jLabel9.setText("Ilość pikseli:");

        textLabel_MainImagePix.setText(" ");

        jLabel10.setText("Rozmiar pliku:");

        textLabel_MainImageSize.setText(" ");

        button_changeMode.setBackground(new java.awt.Color(153, 255, 255));
        button_changeMode.setText("Szyfrowanie");
        button_changeMode.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        button_changeMode.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        button_changeMode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                button_changeModeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout basicPanelLayout = new javax.swing.GroupLayout(basicPanel);
        basicPanel.setLayout(basicPanelLayout);
        basicPanelLayout.setHorizontalGroup(
            basicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basicPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(basicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(button_changeMode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(basicPanelLayout.createSequentialGroup()
                        .addComponent(label_selectedImageView, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(basicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(basicPanelLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textLabel_MainImageY, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(basicPanelLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textLabel_MainImageX, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(basicPanelLayout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textLabel_MainImagePix, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(basicPanelLayout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textLabel_MainImageSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addComponent(text_path_source)
                    .addComponent(button_selectMainImage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        basicPanelLayout.setVerticalGroup(
            basicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basicPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(text_path_source, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_selectMainImage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(basicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_selectedImageView, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(basicPanelLayout.createSequentialGroup()
                        .addGroup(basicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(textLabel_MainImageX))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(basicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(textLabel_MainImageY))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(basicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(textLabel_MainImagePix))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(basicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(textLabel_MainImageSize))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_changeMode)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        codingPanel.setBackground(new java.awt.Color(204, 204, 255));

        text_pathFile.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                text_pathFileActionPerformed(evt);
            }
        });

        button_selectFileToCode.setText("plik do zakodowania");
        button_selectFileToCode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                button_selectFileToCodeActionPerformed(evt);
            }
        });

        jLabel1.setText("Rozmiar wybranego pliku:");

        textLabel_toCodeSize.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        textLabel_toCodeSize.setText("0 KB");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Algorytm:");

        combo_SelectedCodeAlgo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                combo_SelectedCodeAlgoActionPerformed(evt);
            }
        });

        jLabel3.setText("Maksymalna liczba zakodowanych bajtów:");

        textLabel_algoMaxSize.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        textLabel_algoMaxSize.setText("0 KB");

        text_pathOutput.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                text_pathOutputActionPerformed(evt);
            }
        });

        button_selectOutputFile.setText("ścieżka docelowa");
        button_selectOutputFile.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                button_selectOutputFileActionPerformed(evt);
            }
        });

        progresBar_fileUsage.setBackground(new java.awt.Color(204, 204, 204));
        progresBar_fileUsage.setForeground(new java.awt.Color(0, 0, 0));
        progresBar_fileUsage.setStringPainted(true);

        javax.swing.GroupLayout codingPanelLayout = new javax.swing.GroupLayout(codingPanel);
        codingPanel.setLayout(codingPanelLayout);
        codingPanelLayout.setHorizontalGroup(
            codingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(codingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(codingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(text_pathFile)
                    .addComponent(button_selectFileToCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(combo_SelectedCodeAlgo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, codingPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textLabel_toCodeSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(codingPanelLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textLabel_algoMaxSize, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE))
                    .addComponent(text_pathOutput, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(button_selectOutputFile, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progresBar_fileUsage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        codingPanelLayout.setVerticalGroup(
            codingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(codingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(text_pathFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_selectFileToCode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(codingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(textLabel_toCodeSize))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(combo_SelectedCodeAlgo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(codingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(textLabel_algoMaxSize))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(progresBar_fileUsage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(text_pathOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_selectOutputFile)
                .addContainerGap())
        );

        outputPanel.setBackground(new java.awt.Color(204, 204, 255));

        button_process.setText("Szyfruj");
        button_process.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                button_processActionPerformed(evt);
            }
        });

        progressBar.setDoubleBuffered(true);

        textLabel_processDescrypt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textLabel_processDescrypt.setText(" ");
        textLabel_processDescrypt.setDoubleBuffered(true);

        javax.swing.GroupLayout outputPanelLayout = new javax.swing.GroupLayout(outputPanel);
        outputPanel.setLayout(outputPanelLayout);
        outputPanelLayout.setHorizontalGroup(
            outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(button_process, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(textLabel_processDescrypt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        outputPanelLayout.setVerticalGroup(
            outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outputPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(button_process)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textLabel_processDescrypt))
        );

        decodingPanel.setBackground(new java.awt.Color(204, 204, 255));

        button_selectImageToUncode.setText("zakodowany plik");
        button_selectImageToUncode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                button_selectImageToUncodeActionPerformed(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Algorytm:");

        combo_decodeAlgo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        combo_decodeAlgo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                combo_decodeAlgoActionPerformed(evt);
            }
        });

        jLabel7.setText("Szerokość:");

        jLabel8.setText("Wysokość:");

        textLabel_xDecode.setText(" ");

        textLabel_yDecode.setText(" ");

        javax.swing.GroupLayout decodingPanelLayout = new javax.swing.GroupLayout(decodingPanel);
        decodingPanel.setLayout(decodingPanelLayout);
        decodingPanelLayout.setHorizontalGroup(
            decodingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(decodingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(decodingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(text_pathCodeImage)
                    .addComponent(button_selectImageToUncode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(combo_decodeAlgo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(decodingPanelLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textLabel_yDecode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(decodingPanelLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textLabel_xDecode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        decodingPanelLayout.setVerticalGroup(
            decodingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(decodingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(text_pathCodeImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_selectImageToUncode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(decodingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(textLabel_xDecode))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(decodingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(textLabel_yDecode))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(combo_decodeAlgo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(basicPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(codingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(outputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(decodingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(basicPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(codingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(decodingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(outputPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_selectMainImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_selectMainImageActionPerformed
        new FileSelector(FileSelector.SelectorType.OPEN_MAIN).setVisible(true);
    }//GEN-LAST:event_button_selectMainImageActionPerformed

    private void button_selectOutputFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_selectOutputFileActionPerformed
        new FileSelector(FileSelector.SelectorType.SAVE_CODED).setVisible(true);
    }//GEN-LAST:event_button_selectOutputFileActionPerformed

    private void button_processActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_button_processActionPerformed
    {//GEN-HEADEREND:event_button_processActionPerformed

        if (mode)//decoding

            runner.GetAlgorytm().Restore(mainImage, Util.GetImage(text_pathCodeImage.getText()), new File(text_pathCodeImage.getText()));
        else
            runner.GetAlgorytm().Code(mainImage, new File(text_pathFile.getText()), text_pathOutput.getText());

    }//GEN-LAST:event_button_processActionPerformed

    private void combo_SelectedCodeAlgoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_combo_SelectedCodeAlgoActionPerformed
    {//GEN-HEADEREND:event_combo_SelectedCodeAlgoActionPerformed

        selectedAlgo = combo_SelectedCodeAlgo.getSelectedIndex();

        if (selectedAlgo < 0)
            selectedAlgo = 0;
        if (refresher != null)
            refresher.SetAlgo(algorithms[selectedAlgo]);
        if (runner != null)
            runner.SetAlgorytm(algorithms[selectedAlgo]);
        updateCodeInfo();

    }//GEN-LAST:event_combo_SelectedCodeAlgoActionPerformed

    private void text_pathOutputActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_text_pathOutputActionPerformed
    {//GEN-HEADEREND:event_text_pathOutputActionPerformed
        CheckCanProcess();
    }//GEN-LAST:event_text_pathOutputActionPerformed

    private void button_selectFileToCodeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_button_selectFileToCodeActionPerformed
    {//GEN-HEADEREND:event_button_selectFileToCodeActionPerformed
        new FileSelector(FileSelector.SelectorType.OPEN_FILE).setVisible(true);
    }//GEN-LAST:event_button_selectFileToCodeActionPerformed

    private void button_selectImageToUncodeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_button_selectImageToUncodeActionPerformed
    {//GEN-HEADEREND:event_button_selectImageToUncodeActionPerformed
        new FileSelector(FileSelector.SelectorType.OPEN_CODED).setVisible(true);
    }//GEN-LAST:event_button_selectImageToUncodeActionPerformed

    private void combo_decodeAlgoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_combo_decodeAlgoActionPerformed
    {//GEN-HEADEREND:event_combo_decodeAlgoActionPerformed
        selectedAlgo = combo_decodeAlgo.getSelectedIndex();

        if (selectedAlgo < 0)
            selectedAlgo = 0;
        if (refresher != null)
            refresher.SetAlgo(algorithms[selectedAlgo]);
        if (runner != null)
            runner.SetAlgorytm(algorithms[selectedAlgo]);
        updateDecodeInfo();

    }//GEN-LAST:event_combo_decodeAlgoActionPerformed

    private void button_changeModeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_button_changeModeActionPerformed
    {//GEN-HEADEREND:event_button_changeModeActionPerformed
        if (mode)
        {//jezeli zmieniemy na szyfrowanie
            decodingPanel.hide();
            codingPanel.show();
            button_changeMode.setText("Szyfrowanie");
            button_process.setText("Szyfruj");
        }
        else
        {
            decodingPanel.show();
            codingPanel.hide();
            button_changeMode.setText("Deszyfrowanie");
            button_process.setText("deszyfruj");
        }
        mode = !mode;
        CheckCanProcess();
    }//GEN-LAST:event_button_changeModeActionPerformed

    private void text_path_sourceActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_text_path_sourceActionPerformed
    {//GEN-HEADEREND:event_text_path_sourceActionPerformed
        main.mainImage = Util.GetImage(new File(text_path_source.getText()));
        main.updateMainImageInfo();
    }//GEN-LAST:event_text_path_sourceActionPerformed

    private void text_pathFileActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_text_pathFileActionPerformed
    {//GEN-HEADEREND:event_text_pathFileActionPerformed
        main.updateCodeInfo();
    }//GEN-LAST:event_text_pathFileActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
        }
        catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                main = new MainWindow();
                main.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel basicPanel;
    private javax.swing.JButton button_changeMode;
    private javax.swing.JButton button_process;
    private javax.swing.JButton button_selectFileToCode;
    private javax.swing.JButton button_selectImageToUncode;
    private javax.swing.JButton button_selectMainImage;
    private javax.swing.JButton button_selectOutputFile;
    private javax.swing.JPanel codingPanel;
    private javax.swing.JComboBox combo_SelectedCodeAlgo;
    private javax.swing.JComboBox combo_decodeAlgo;
    private javax.swing.JPanel decodingPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel label_selectedImageView;
    private javax.swing.JPanel outputPanel;
    private javax.swing.JProgressBar progresBar_fileUsage;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel textLabel_MainImagePix;
    private javax.swing.JLabel textLabel_MainImageSize;
    private javax.swing.JLabel textLabel_MainImageX;
    private javax.swing.JLabel textLabel_MainImageY;
    private javax.swing.JLabel textLabel_algoMaxSize;
    private javax.swing.JLabel textLabel_processDescrypt;
    private javax.swing.JLabel textLabel_toCodeSize;
    private javax.swing.JLabel textLabel_xDecode;
    private javax.swing.JLabel textLabel_yDecode;
    private javax.swing.JTextField text_pathCodeImage;
    private javax.swing.JTextField text_pathFile;
    private javax.swing.JTextField text_pathOutput;
    private javax.swing.JTextField text_path_source;
    // End of variables declaration//GEN-END:variables
}
