/*
 * SelerShopView.java
 */

package selershop;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import selershop.Plotno;

/**
 * The application's main frame.
 */
public class SelerShopView extends FrameView {
    
    public SelerShopView(SingleFrameApplication app) {
        super(app);

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        /*if (aboutBox == null) {
            JFrame mainFrame = SelerShopApp.getApplication().getMainFrame();
            aboutBox = new SelerShopAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        SelerShopApp.getApplication().show(aboutBox);*/
    }
    
    public boolean pobierzWartosc() {
        String wartosc_str = JOptionPane.showInputDialog(null, "Wpisz wartość : ", "SelerShop - Wpisz wartość", 1);
        try { wartosc = new Float(wartosc_str);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Pomyśl zanim wpiszesz! " + wartosc_str + " to nie liczba.", "SelerShop", 1);
        }
        if(wartosc_str != null) {
            // pobrano
            return true;
        }
        else {
            // niepobrano
            return false;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jScrollPane = new javax.swing.JScrollPane();
        plotno = new selershop.Plotno();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        otworz = new javax.swing.JMenuItem();
        zapisz = new javax.swing.JMenuItem();
        resetuj = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        dodawanie = new javax.swing.JMenuItem();
        mnozenie = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        usredniajacy = new javax.swing.JMenuItem();
        lp2 = new javax.swing.JMenuItem();
        gauss = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        gorny1 = new javax.swing.JMenuItem();
        gorny2 = new javax.swing.JMenuItem();
        gorny3 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        krawedziePoziomy = new javax.swing.JMenuItem();
        krawedziePionowy = new javax.swing.JMenuItem();
        laplacian = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        stiffness = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jDialog1 = new javax.swing.JDialog();
        jDialog2 = new javax.swing.JDialog();
        popupMenu1 = new java.awt.PopupMenu();
        jDialog3 = new javax.swing.JDialog();
        jDialog4 = new javax.swing.JDialog();
        popupMenu2 = new java.awt.PopupMenu();

        mainPanel.setName("mainPanel"); // NOI18N

        jScrollPane.setName("jScrollPane"); // NOI18N

        plotno.setName("plotno"); // NOI18N

        javax.swing.GroupLayout plotnoLayout = new javax.swing.GroupLayout(plotno);
        plotno.setLayout(plotnoLayout);
        plotnoLayout.setHorizontalGroup(
            plotnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 398, Short.MAX_VALUE)
        );
        plotnoLayout.setVerticalGroup(
            plotnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 252, Short.MAX_VALUE)
        );

        jScrollPane.setViewportView(plotno);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(selershop.SelerShopApp.class).getContext().getResourceMap(SelerShopView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        otworz.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        otworz.setText(resourceMap.getString("otworz.text")); // NOI18N
        otworz.setName("otworz"); // NOI18N
        otworz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                otworzActionPerformed(evt);
            }
        });
        fileMenu.add(otworz);

        zapisz.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        zapisz.setText(resourceMap.getString("zapisz.text")); // NOI18N
        zapisz.setName("zapisz"); // NOI18N
        zapisz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zapiszActionPerformed(evt);
            }
        });
        fileMenu.add(zapisz);

        resetuj.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        resetuj.setText(resourceMap.getString("resetuj.text")); // NOI18N
        resetuj.setName("resetuj"); // NOI18N
        resetuj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetujActionPerformed(evt);
            }
        });
        fileMenu.add(resetuj);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(selershop.SelerShopApp.class).getContext().getActionMap(SelerShopView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N

        dodawanie.setText(resourceMap.getString("dodawanie.text")); // NOI18N
        dodawanie.setName("dodawanie"); // NOI18N
        dodawanie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dodawanieActionPerformed(evt);
            }
        });
        jMenu1.add(dodawanie);

        mnozenie.setText(resourceMap.getString("mnozenie.text")); // NOI18N
        mnozenie.setName("mnozenie"); // NOI18N
        mnozenie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnozenieActionPerformed(evt);
            }
        });
        jMenu1.add(mnozenie);

        menuBar.add(jMenu1);

        jMenu2.setText(resourceMap.getString("jMenu2.text")); // NOI18N
        jMenu2.setName("jMenu2"); // NOI18N

        usredniajacy.setText(resourceMap.getString("usredniajacy.text")); // NOI18N
        usredniajacy.setName("usredniajacy"); // NOI18N
        usredniajacy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usredniajacyActionPerformed(evt);
            }
        });
        jMenu2.add(usredniajacy);

        lp2.setText(resourceMap.getString("lp2.text")); // NOI18N
        lp2.setName("lp2"); // NOI18N
        lp2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lp2ActionPerformed(evt);
            }
        });
        jMenu2.add(lp2);

        gauss.setText(resourceMap.getString("gauss.text")); // NOI18N
        gauss.setName("gauss"); // NOI18N
        gauss.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gaussActionPerformed(evt);
            }
        });
        jMenu2.add(gauss);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jMenu2.add(jSeparator1);

        gorny1.setText(resourceMap.getString("gorny1.text")); // NOI18N
        gorny1.setName("gorny1"); // NOI18N
        gorny1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gorny1ActionPerformed(evt);
            }
        });
        jMenu2.add(gorny1);

        gorny2.setText(resourceMap.getString("gorny2.text")); // NOI18N
        gorny2.setName("gorny2"); // NOI18N
        gorny2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gorny2ActionPerformed(evt);
            }
        });
        jMenu2.add(gorny2);

        gorny3.setText(resourceMap.getString("gorny3.text")); // NOI18N
        gorny3.setName("gorny3"); // NOI18N
        gorny3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gorny3ActionPerformed(evt);
            }
        });
        jMenu2.add(gorny3);

        jSeparator2.setName("jSeparator2"); // NOI18N
        jMenu2.add(jSeparator2);

        krawedziePoziomy.setText(resourceMap.getString("krawedziePoziomy.text")); // NOI18N
        krawedziePoziomy.setName("krawedziePoziomy"); // NOI18N
        krawedziePoziomy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                krawedziePoziomyActionPerformed(evt);
            }
        });
        jMenu2.add(krawedziePoziomy);

        krawedziePionowy.setText(resourceMap.getString("krawedziePionowy.text")); // NOI18N
        krawedziePionowy.setName("krawedziePionowy"); // NOI18N
        krawedziePionowy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                krawedziePionowyActionPerformed(evt);
            }
        });
        jMenu2.add(krawedziePionowy);

        laplacian.setText(resourceMap.getString("laplacian.text")); // NOI18N
        laplacian.setName("laplacian"); // NOI18N
        laplacian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                laplacianActionPerformed(evt);
            }
        });
        jMenu2.add(laplacian);

        jSeparator3.setName("jSeparator3"); // NOI18N
        jMenu2.add(jSeparator3);

        stiffness.setText(resourceMap.getString("stiffness.text")); // NOI18N
        stiffness.setName("stiffness"); // NOI18N
        stiffness.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stiffnessActionPerformed(evt);
            }
        });
        jMenu2.add(stiffness);

        menuBar.add(jMenu2);

        jMenu3.setText(resourceMap.getString("jMenu3.text")); // NOI18N
        jMenu3.setName("jMenu3"); // NOI18N

        jMenuItem5.setText(resourceMap.getString("jMenuItem5.text")); // NOI18N
        jMenuItem5.setName("jMenuItem5"); // NOI18N
        jMenu3.add(jMenuItem5);

        jMenuItem6.setText(resourceMap.getString("jMenuItem6.text")); // NOI18N
        jMenuItem6.setName("jMenuItem6"); // NOI18N
        jMenu3.add(jMenuItem6);

        menuBar.add(jMenu3);

        jMenu4.setText(resourceMap.getString("jMenu4.text")); // NOI18N
        jMenu4.setName("jMenu4"); // NOI18N
        menuBar.add(jMenu4);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 230, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        jDialog1.setName("jDialog1"); // NOI18N

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jDialog2.setName("jDialog2"); // NOI18N

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        popupMenu1.setLabel(resourceMap.getString("popupMenu1.label")); // NOI18N

        jDialog3.setName("jDialog3"); // NOI18N

        javax.swing.GroupLayout jDialog3Layout = new javax.swing.GroupLayout(jDialog3.getContentPane());
        jDialog3.getContentPane().setLayout(jDialog3Layout);
        jDialog3Layout.setHorizontalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog3Layout.setVerticalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jDialog4.setName("jDialog4"); // NOI18N

        javax.swing.GroupLayout jDialog4Layout = new javax.swing.GroupLayout(jDialog4.getContentPane());
        jDialog4.getContentPane().setLayout(jDialog4Layout);
        jDialog4Layout.setHorizontalGroup(
            jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog4Layout.setVerticalGroup(
            jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        popupMenu2.setLabel(resourceMap.getString("popupMenu2.label")); // NOI18N

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void otworzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_otworzActionPerformed
        try {
            plotno.Otworz();
        } catch (IOException ex) {
            Logger.getLogger(SelerShopView.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }//GEN-LAST:event_otworzActionPerformed

    private void resetujActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetujActionPerformed
        try {        
            plotno.Resetuj();
        } catch (IOException ex) {
            Logger.getLogger(SelerShopView.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }//GEN-LAST:event_resetujActionPerformed

    private void zapiszActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zapiszActionPerformed
        try {        
            plotno.Zapisz();
        } catch (IOException ex) {
            Logger.getLogger(SelerShopView.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }//GEN-LAST:event_zapiszActionPerformed

    private void dodawanieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dodawanieActionPerformed
        if(this.pobierzWartosc()){
            Float a = new Float(1);
            plotno.Punkt(a, wartosc);
        }
        //JOptionPane.showMessageDialog(null, wartosc, "SelerShop", 1);
    }//GEN-LAST:event_dodawanieActionPerformed

    private void mnozenieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnozenieActionPerformed
        if(this.pobierzWartosc()){
            Float a = new Float(0);
            plotno.Punkt(wartosc, a);
        }
    }//GEN-LAST:event_mnozenieActionPerformed

    private void gaussActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gaussActionPerformed
        // http://en.wikipedia.org/wiki/Gaussian_blur#Sample_Gaussian_matrix
        float[] matrix = {
            0.00000067f, 0.00002292f, 0.00019117f, 0.00038771f, 0.00019117f, 0.00002292f, 0.00000067f,
            0.00002292f, 0.00078633f, 0.00655965f, 0.01330373f, 0.00655965f, 0.00078633f, 0.00002292f,
            0.00019117f, 0.00655965f, 0.05472157f, 0.11098164f, 0.05472157f, 0.00655965f, 0.00019117f,
            0.00038771f, 0.01330373f, 0.11098164f, 0.22508352f, 0.11098164f, 0.01330373f, 0.00038771f,
            0.00019117f, 0.00655965f, 0.05472157f, 0.11098164f, 0.05472157f, 0.00655965f, 0.00019117f,
            0.00002292f, 0.00078633f, 0.00655965f, 0.01330373f, 0.00655965f, 0.00078633f, 0.00002292f,
            0.00000067f, 0.00002292f, 0.00019117f, 0.00038771f, 0.00019117f, 0.00002292f, 0.00000067f
        };
        int rozmiar = 7;
        plotno.Splot(matrix, rozmiar);
    }//GEN-LAST:event_gaussActionPerformed

    private void laplacianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_laplacianActionPerformed
        // http://en.wikipedia.org/wiki/Laplacian_matrix
        float[] matrix = {
            2f, -1f, 0f, 0f, -1f, 0f,
            -1f, 3f, -1f, 0f, -1f, 0f,
            0f, -1f, 2f, -1f, 0f, 0f, 
            0f, 0f, -1f, 3f, -1f, -1f, 
            -1f, -1f, 0f, -1f, 3f, 0f, 
            0f, 0f, 0f, -1f, 0f, 1f
        };
        int rozmiar = 6;
        plotno.Splot(matrix, rozmiar);
    }//GEN-LAST:event_laplacianActionPerformed

    private void usredniajacyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usredniajacyActionPerformed
        // http://www.projektydoszkoly.za.pl/index.html#dolno
        float[] matrix = {
            1/9f,1/9f,1/9f,
            1/9f,1/9f,1/9f,
            1/9f,1/9f,1/9f
        };
        int rozmiar = 3;
        plotno.Splot(matrix, rozmiar);
    }//GEN-LAST:event_usredniajacyActionPerformed

    private void lp2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lp2ActionPerformed
        // http://www.projektydoszkoly.za.pl/index.html#dolno
        float[] matrix = {
            1/16f,1/16f,1/16f,
            1/16f,4/16f,1/16f,
            1/16f,1/16f,1/16f
        };
        int rozmiar = 3;
        plotno.Splot(matrix, rozmiar);
    }//GEN-LAST:event_lp2ActionPerformed

    private void gorny1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gorny1ActionPerformed
        // http://www.projektydoszkoly.za.pl/index.html#dolno
        float[] matrix = {
            -1f,-1f,-1f,
            -1f,9f,-1f,
            -1f,-1f,-1f
        };
        int rozmiar = 3;
        plotno.Splot(matrix, rozmiar);
    }//GEN-LAST:event_gorny1ActionPerformed

    private void gorny2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gorny2ActionPerformed
        // http://www.projektydoszkoly.za.pl/index.html#dolno
        float[] matrix = {
            1f,-2f,1f,
            -2f,5f,-2f,
            1f,-2f,1f
        };
        int rozmiar = 3;
        plotno.Splot(matrix, rozmiar);
    }//GEN-LAST:event_gorny2ActionPerformed

    private void gorny3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gorny3ActionPerformed
        // http://www.projektydoszkoly.za.pl/index.html#dolno
        float[] matrix = {
            0f,-1/16f,0f,
            -1/16f,20/16f,-1/16f,
            0f,-1/16f,0f
        };
        int rozmiar = 3;
        plotno.Splot(matrix, rozmiar);
    }//GEN-LAST:event_gorny3ActionPerformed

    private void krawedziePoziomyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_krawedziePoziomyActionPerformed
        // http://www.projektydoszkoly.za.pl/index.html#dolno
        float[] matrix = {
            0f,-1f,0f,
            0f,1f,0f,
            0f,0f,0f
        };
        int rozmiar = 3;
        plotno.Splot(matrix, rozmiar);
    }//GEN-LAST:event_krawedziePoziomyActionPerformed

    private void krawedziePionowyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_krawedziePionowyActionPerformed
        // http://www.projektydoszkoly.za.pl/index.html#dolno
        float[] matrix = {
            0f,0f,0f,
            -1f,1f,0f,
            0f,0f,0f
        };
        int rozmiar = 3;
        plotno.Splot(matrix, rozmiar);
    }//GEN-LAST:event_krawedziePionowyActionPerformed

    private void stiffnessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stiffnessActionPerformed
        // http://en.wikipedia.org/wiki/Stiffness_matrix
        float[] matrix = {
            13f,-1f, 0f, 0f, -12f, 0f, 
            -1f, 3f, -1f, 0f, -1f, 0f, 
            0f, -1f, 2f, -1f, 0f, 0f, 
            0f, 0f, -1f, 3f, -1f, -1f, 
            -12f, -1f, 0f, -1f, 14f, 0f, 
            0f, 0f, 0f, -1f, 0f, 1f
        };
        int rozmiar = 6;
        plotno.Splot(matrix, rozmiar);
    }//GEN-LAST:event_stiffnessActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem dodawanie;
    private javax.swing.JMenuItem gauss;
    private javax.swing.JMenuItem gorny1;
    private javax.swing.JMenuItem gorny2;
    private javax.swing.JMenuItem gorny3;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JDialog jDialog3;
    private javax.swing.JDialog jDialog4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JMenuItem krawedziePionowy;
    private javax.swing.JMenuItem krawedziePoziomy;
    private javax.swing.JMenuItem laplacian;
    private javax.swing.JMenuItem lp2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem mnozenie;
    private javax.swing.JMenuItem otworz;
    private selershop.Plotno plotno;
    private java.awt.PopupMenu popupMenu1;
    private java.awt.PopupMenu popupMenu2;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenuItem resetuj;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JMenuItem stiffness;
    private javax.swing.JMenuItem usredniajacy;
    private javax.swing.JMenuItem zapisz;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
    private Float wartosc;
}