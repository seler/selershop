/*
 * SelerShopView.java
 */

package selershop;

import java.awt.Color;
import java.awt.Component;
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
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import selershop.Plotno;

/**
 * The application's main frame.
 */
public class SelerShopView extends FrameView {
    
    public SelerShopView(SingleFrameApplication app) {
        super(app);

        initComponents();
        ustawInputy();
        //HistogramKanal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Wszystkie", "Czerwony", "Zielony", "Niebieski" }));
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
    
    private void ustawInputy(){
        int x = Integer.parseInt(rozmiarXSpinner.getValue().toString());
        int y = Integer.parseInt(rozmiarYSpinner.getValue().toString());
        int a,b;
        for (Component c : panelMacierzy.getComponents()){
            a = Integer.parseInt(((JTextField)c).getName().substring(8, 9));
            b = Integer.parseInt(((JTextField)c).getName().substring(7, 8));
            if(a < x && b < y){
                ((JTextField)c).setEnabled(true);
            } else {
                ((JTextField)c).setEnabled(false);
            }
        }
    }
    
    private void pobierzMacierz(){
        rozmiarX = Integer.parseInt(rozmiarXSpinner.getValue().toString());
        rozmiarY = Integer.parseInt(rozmiarYSpinner.getValue().toString());
        float[] m = new float[rozmiarX*rozmiarY];
        int a=0,b=0, i=0, j=0;
        for (Component c : panelMacierzy.getComponents()){
            a = Integer.parseInt(((JTextField)c).getName().substring(8, 9));
            b = Integer.parseInt(((JTextField)c).getName().substring(7, 8));
            if(a < rozmiarX && b < rozmiarY){
                m[b*rozmiarX+a]= Float.parseFloat(((JTextField)c).getText());
                System.out.println("x="+a + ", y="+b+", z="+ Float.parseFloat(((JTextField)c).getText()));
            }
            i++; j++;
            if(i >= rozmiarX) i = 0;
            if(j >= rozmiarY) j = 0;
        }
        macierz =  m;
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panelnahistogramy = new javax.swing.JPanel();
        histogramPanel = new myprzegladarka.histogramPanel();
        histogramPanelR = new myprzegladarka.histogramPanel();
        histogramPanelG = new myprzegladarka.histogramPanel();
        histogramPanelB = new myprzegladarka.histogramPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        rozmiarXSpinner = new javax.swing.JSpinner();
        rozmiarYSpinner = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        panelMacierzy = new javax.swing.JPanel();
        macierz00 = new javax.swing.JTextField();
        macierz01 = new javax.swing.JTextField();
        macierz02 = new javax.swing.JTextField();
        macierz03 = new javax.swing.JTextField();
        macierz04 = new javax.swing.JTextField();
        macierz05 = new javax.swing.JTextField();
        macierz06 = new javax.swing.JTextField();
        macierz07 = new javax.swing.JTextField();
        macierz08 = new javax.swing.JTextField();
        macierz10 = new javax.swing.JTextField();
        macierz11 = new javax.swing.JTextField();
        macierz12 = new javax.swing.JTextField();
        macierz13 = new javax.swing.JTextField();
        macierz14 = new javax.swing.JTextField();
        macierz15 = new javax.swing.JTextField();
        macierz16 = new javax.swing.JTextField();
        macierz17 = new javax.swing.JTextField();
        macierz18 = new javax.swing.JTextField();
        macierz20 = new javax.swing.JTextField();
        macierz21 = new javax.swing.JTextField();
        macierz22 = new javax.swing.JTextField();
        macierz23 = new javax.swing.JTextField();
        macierz24 = new javax.swing.JTextField();
        macierz25 = new javax.swing.JTextField();
        macierz26 = new javax.swing.JTextField();
        macierz27 = new javax.swing.JTextField();
        macierz28 = new javax.swing.JTextField();
        macierz30 = new javax.swing.JTextField();
        macierz31 = new javax.swing.JTextField();
        macierz32 = new javax.swing.JTextField();
        macierz33 = new javax.swing.JTextField();
        macierz34 = new javax.swing.JTextField();
        macierz35 = new javax.swing.JTextField();
        macierz36 = new javax.swing.JTextField();
        macierz37 = new javax.swing.JTextField();
        macierz38 = new javax.swing.JTextField();
        macierz40 = new javax.swing.JTextField();
        macierz41 = new javax.swing.JTextField();
        macierz42 = new javax.swing.JTextField();
        macierz43 = new javax.swing.JTextField();
        macierz44 = new javax.swing.JTextField();
        macierz45 = new javax.swing.JTextField();
        macierz46 = new javax.swing.JTextField();
        macierz47 = new javax.swing.JTextField();
        macierz48 = new javax.swing.JTextField();
        macierz50 = new javax.swing.JTextField();
        macierz51 = new javax.swing.JTextField();
        macierz52 = new javax.swing.JTextField();
        macierz53 = new javax.swing.JTextField();
        macierz54 = new javax.swing.JTextField();
        macierz55 = new javax.swing.JTextField();
        macierz56 = new javax.swing.JTextField();
        macierz57 = new javax.swing.JTextField();
        macierz58 = new javax.swing.JTextField();
        macierz68 = new javax.swing.JTextField();
        macierz67 = new javax.swing.JTextField();
        macierz66 = new javax.swing.JTextField();
        macierz65 = new javax.swing.JTextField();
        macierz64 = new javax.swing.JTextField();
        macierz63 = new javax.swing.JTextField();
        macierz62 = new javax.swing.JTextField();
        macierz61 = new javax.swing.JTextField();
        macierz60 = new javax.swing.JTextField();
        macierz78 = new javax.swing.JTextField();
        macierz77 = new javax.swing.JTextField();
        macierz76 = new javax.swing.JTextField();
        macierz75 = new javax.swing.JTextField();
        macierz74 = new javax.swing.JTextField();
        macierz73 = new javax.swing.JTextField();
        macierz72 = new javax.swing.JTextField();
        macierz71 = new javax.swing.JTextField();
        macierz70 = new javax.swing.JTextField();
        macierz88 = new javax.swing.JTextField();
        macierz87 = new javax.swing.JTextField();
        macierz86 = new javax.swing.JTextField();
        macierz85 = new javax.swing.JTextField();
        macierz84 = new javax.swing.JTextField();
        macierz83 = new javax.swing.JTextField();
        macierz82 = new javax.swing.JTextField();
        macierz80 = new javax.swing.JTextField();
        macierz81 = new javax.swing.JTextField();
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
        wlasny = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        erozja = new javax.swing.JMenuItem();
        dylatacja = new javax.swing.JMenuItem();
        otwarcie = new javax.swing.JMenuItem();
        zamkniecie = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        wyrownanie = new javax.swing.JMenuItem();
        binaryzacja = new javax.swing.JMenuItem();
        obliczHistogram = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        dodajobraz = new javax.swing.JMenuItem();
        odejmijobraz = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        jScrollPane.setName("jScrollPane"); // NOI18N

        plotno.setName("plotno"); // NOI18N

        javax.swing.GroupLayout plotnoLayout = new javax.swing.GroupLayout(plotno);
        plotno.setLayout(plotnoLayout);
        plotnoLayout.setHorizontalGroup(
            plotnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 797, Short.MAX_VALUE)
        );
        plotnoLayout.setVerticalGroup(
            plotnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 562, Short.MAX_VALUE)
        );

        jScrollPane.setViewportView(plotno);

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        panelnahistogramy.setName("panelnahistogramy"); // NOI18N

        histogramPanel.setName("histogramPanel"); // NOI18N

        javax.swing.GroupLayout histogramPanelLayout = new javax.swing.GroupLayout(histogramPanel);
        histogramPanel.setLayout(histogramPanelLayout);
        histogramPanelLayout.setHorizontalGroup(
            histogramPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 407, Short.MAX_VALUE)
        );
        histogramPanelLayout.setVerticalGroup(
            histogramPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );

        histogramPanelR.setName("histogramPanelR"); // NOI18N

        javax.swing.GroupLayout histogramPanelRLayout = new javax.swing.GroupLayout(histogramPanelR);
        histogramPanelR.setLayout(histogramPanelRLayout);
        histogramPanelRLayout.setHorizontalGroup(
            histogramPanelRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 407, Short.MAX_VALUE)
        );
        histogramPanelRLayout.setVerticalGroup(
            histogramPanelRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );

        histogramPanelG.setName("histogramPanelG"); // NOI18N

        javax.swing.GroupLayout histogramPanelGLayout = new javax.swing.GroupLayout(histogramPanelG);
        histogramPanelG.setLayout(histogramPanelGLayout);
        histogramPanelGLayout.setHorizontalGroup(
            histogramPanelGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 407, Short.MAX_VALUE)
        );
        histogramPanelGLayout.setVerticalGroup(
            histogramPanelGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );

        histogramPanelB.setName("histogramPanelB"); // NOI18N

        javax.swing.GroupLayout histogramPanelBLayout = new javax.swing.GroupLayout(histogramPanelB);
        histogramPanelB.setLayout(histogramPanelBLayout);
        histogramPanelBLayout.setHorizontalGroup(
            histogramPanelBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 407, Short.MAX_VALUE)
        );
        histogramPanelBLayout.setVerticalGroup(
            histogramPanelBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelnahistogramyLayout = new javax.swing.GroupLayout(panelnahistogramy);
        panelnahistogramy.setLayout(panelnahistogramyLayout);
        panelnahistogramyLayout.setHorizontalGroup(
            panelnahistogramyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelnahistogramyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelnahistogramyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelnahistogramyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(histogramPanelR, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(histogramPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(histogramPanelB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(histogramPanelG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelnahistogramyLayout.setVerticalGroup(
            panelnahistogramyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelnahistogramyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(histogramPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(histogramPanelR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(histogramPanelB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(histogramPanelG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(selershop.SelerShopApp.class).getContext().getResourceMap(SelerShopView.class);
        jTabbedPane1.addTab(resourceMap.getString("panelnahistogramy.TabConstraints.tabTitle"), panelnahistogramy); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        rozmiarXSpinner.setModel(new javax.swing.SpinnerNumberModel(3, 1, 9, 1));
        rozmiarXSpinner.setName("rozmiarXSpinner"); // NOI18N
        rozmiarXSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rozmiarXSpinnerStateChanged(evt);
            }
        });

        rozmiarYSpinner.setModel(new javax.swing.SpinnerNumberModel(3, 1, 9, 1));
        rozmiarYSpinner.setName("rozmiarYSpinner"); // NOI18N
        rozmiarYSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rozmiarYSpinnerStateChanged(evt);
            }
        });

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        panelMacierzy.setName("panelMacierzy"); // NOI18N

        macierz00.setText(resourceMap.getString("macierz00.text")); // NOI18N
        macierz00.setName("macierz00"); // NOI18N

        macierz01.setText(resourceMap.getString("macierz01.text")); // NOI18N
        macierz01.setName("macierz01"); // NOI18N

        macierz02.setText(resourceMap.getString("macierz02.text")); // NOI18N
        macierz02.setName("macierz02"); // NOI18N

        macierz03.setText(resourceMap.getString("macierz03.text")); // NOI18N
        macierz03.setName("macierz03"); // NOI18N

        macierz04.setText(resourceMap.getString("macierz04.text")); // NOI18N
        macierz04.setName("macierz04"); // NOI18N

        macierz05.setText(resourceMap.getString("macierz05.text")); // NOI18N
        macierz05.setName("macierz05"); // NOI18N

        macierz06.setText(resourceMap.getString("macierz06.text")); // NOI18N
        macierz06.setName("macierz06"); // NOI18N

        macierz07.setText(resourceMap.getString("macierz07.text")); // NOI18N
        macierz07.setName("macierz07"); // NOI18N

        macierz08.setText(resourceMap.getString("macierz08.text")); // NOI18N
        macierz08.setName("macierz08"); // NOI18N

        macierz10.setText(resourceMap.getString("macierz10.text")); // NOI18N
        macierz10.setName("macierz10"); // NOI18N

        macierz11.setText(resourceMap.getString("macierz11.text")); // NOI18N
        macierz11.setName("macierz11"); // NOI18N

        macierz12.setText(resourceMap.getString("macierz12.text")); // NOI18N
        macierz12.setName("macierz12"); // NOI18N

        macierz13.setText(resourceMap.getString("macierz13.text")); // NOI18N
        macierz13.setName("macierz13"); // NOI18N

        macierz14.setText(resourceMap.getString("macierz14.text")); // NOI18N
        macierz14.setName("macierz14"); // NOI18N

        macierz15.setText(resourceMap.getString("macierz15.text")); // NOI18N
        macierz15.setName("macierz15"); // NOI18N

        macierz16.setText(resourceMap.getString("macierz16.text")); // NOI18N
        macierz16.setName("macierz16"); // NOI18N

        macierz17.setText(resourceMap.getString("macierz17.text")); // NOI18N
        macierz17.setName("macierz17"); // NOI18N

        macierz18.setText(resourceMap.getString("macierz18.text")); // NOI18N
        macierz18.setName("macierz18"); // NOI18N

        macierz20.setText(resourceMap.getString("macierz20.text")); // NOI18N
        macierz20.setName("macierz20"); // NOI18N

        macierz21.setText(resourceMap.getString("macierz21.text")); // NOI18N
        macierz21.setName("macierz21"); // NOI18N

        macierz22.setText(resourceMap.getString("macierz22.text")); // NOI18N
        macierz22.setName("macierz22"); // NOI18N

        macierz23.setText(resourceMap.getString("macierz23.text")); // NOI18N
        macierz23.setName("macierz23"); // NOI18N

        macierz24.setText(resourceMap.getString("macierz24.text")); // NOI18N
        macierz24.setName("macierz24"); // NOI18N

        macierz25.setText(resourceMap.getString("macierz25.text")); // NOI18N
        macierz25.setName("macierz25"); // NOI18N

        macierz26.setText(resourceMap.getString("macierz26.text")); // NOI18N
        macierz26.setName("macierz26"); // NOI18N

        macierz27.setText(resourceMap.getString("macierz27.text")); // NOI18N
        macierz27.setName("macierz27"); // NOI18N

        macierz28.setText(resourceMap.getString("macierz28.text")); // NOI18N
        macierz28.setName("macierz28"); // NOI18N

        macierz30.setText(resourceMap.getString("macierz30.text")); // NOI18N
        macierz30.setName("macierz30"); // NOI18N

        macierz31.setText(resourceMap.getString("macierz31.text")); // NOI18N
        macierz31.setName("macierz31"); // NOI18N

        macierz32.setText(resourceMap.getString("macierz32.text")); // NOI18N
        macierz32.setName("macierz32"); // NOI18N

        macierz33.setText(resourceMap.getString("macierz33.text")); // NOI18N
        macierz33.setName("macierz33"); // NOI18N

        macierz34.setText(resourceMap.getString("macierz34.text")); // NOI18N
        macierz34.setName("macierz34"); // NOI18N

        macierz35.setText(resourceMap.getString("macierz35.text")); // NOI18N
        macierz35.setName("macierz35"); // NOI18N

        macierz36.setText(resourceMap.getString("macierz36.text")); // NOI18N
        macierz36.setName("macierz36"); // NOI18N

        macierz37.setText(resourceMap.getString("macierz37.text")); // NOI18N
        macierz37.setName("macierz37"); // NOI18N

        macierz38.setText(resourceMap.getString("macierz38.text")); // NOI18N
        macierz38.setName("macierz38"); // NOI18N

        macierz40.setText(resourceMap.getString("macierz40.text")); // NOI18N
        macierz40.setName("macierz40"); // NOI18N

        macierz41.setText(resourceMap.getString("macierz41.text")); // NOI18N
        macierz41.setName("macierz41"); // NOI18N

        macierz42.setText(resourceMap.getString("macierz42.text")); // NOI18N
        macierz42.setName("macierz42"); // NOI18N

        macierz43.setText(resourceMap.getString("macierz43.text")); // NOI18N
        macierz43.setName("macierz43"); // NOI18N

        macierz44.setText(resourceMap.getString("macierz44.text")); // NOI18N
        macierz44.setName("macierz44"); // NOI18N

        macierz45.setText(resourceMap.getString("macierz45.text")); // NOI18N
        macierz45.setName("macierz45"); // NOI18N

        macierz46.setText(resourceMap.getString("macierz46.text")); // NOI18N
        macierz46.setName("macierz46"); // NOI18N

        macierz47.setText(resourceMap.getString("macierz47.text")); // NOI18N
        macierz47.setName("macierz47"); // NOI18N

        macierz48.setText(resourceMap.getString("macierz48.text")); // NOI18N
        macierz48.setName("macierz48"); // NOI18N

        macierz50.setText(resourceMap.getString("macierz50.text")); // NOI18N
        macierz50.setName("macierz50"); // NOI18N

        macierz51.setText(resourceMap.getString("macierz51.text")); // NOI18N
        macierz51.setName("macierz51"); // NOI18N

        macierz52.setText(resourceMap.getString("macierz52.text")); // NOI18N
        macierz52.setName("macierz52"); // NOI18N

        macierz53.setText(resourceMap.getString("macierz53.text")); // NOI18N
        macierz53.setName("macierz53"); // NOI18N

        macierz54.setText(resourceMap.getString("macierz54.text")); // NOI18N
        macierz54.setName("macierz54"); // NOI18N

        macierz55.setText(resourceMap.getString("macierz55.text")); // NOI18N
        macierz55.setName("macierz55"); // NOI18N

        macierz56.setText(resourceMap.getString("macierz56.text")); // NOI18N
        macierz56.setName("macierz56"); // NOI18N

        macierz57.setText(resourceMap.getString("macierz57.text")); // NOI18N
        macierz57.setName("macierz57"); // NOI18N

        macierz58.setText(resourceMap.getString("macierz58.text")); // NOI18N
        macierz58.setName("macierz58"); // NOI18N

        macierz68.setText(resourceMap.getString("macierz68.text")); // NOI18N
        macierz68.setName("macierz68"); // NOI18N

        macierz67.setText(resourceMap.getString("macierz67.text")); // NOI18N
        macierz67.setName("macierz67"); // NOI18N

        macierz66.setText(resourceMap.getString("macierz66.text")); // NOI18N
        macierz66.setName("macierz66"); // NOI18N

        macierz65.setText(resourceMap.getString("macierz65.text")); // NOI18N
        macierz65.setName("macierz65"); // NOI18N

        macierz64.setText(resourceMap.getString("macierz64.text")); // NOI18N
        macierz64.setName("macierz64"); // NOI18N

        macierz63.setText(resourceMap.getString("macierz63.text")); // NOI18N
        macierz63.setName("macierz63"); // NOI18N

        macierz62.setText(resourceMap.getString("macierz62.text")); // NOI18N
        macierz62.setName("macierz62"); // NOI18N

        macierz61.setText(resourceMap.getString("macierz61.text")); // NOI18N
        macierz61.setName("macierz61"); // NOI18N

        macierz60.setText(resourceMap.getString("macierz60.text")); // NOI18N
        macierz60.setName("macierz60"); // NOI18N

        macierz78.setText(resourceMap.getString("macierz78.text")); // NOI18N
        macierz78.setName("macierz78"); // NOI18N

        macierz77.setText(resourceMap.getString("macierz77.text")); // NOI18N
        macierz77.setName("macierz77"); // NOI18N

        macierz76.setText(resourceMap.getString("macierz76.text")); // NOI18N
        macierz76.setName("macierz76"); // NOI18N

        macierz75.setText(resourceMap.getString("macierz75.text")); // NOI18N
        macierz75.setName("macierz75"); // NOI18N

        macierz74.setText(resourceMap.getString("macierz74.text")); // NOI18N
        macierz74.setName("macierz74"); // NOI18N

        macierz73.setText(resourceMap.getString("macierz73.text")); // NOI18N
        macierz73.setName("macierz73"); // NOI18N

        macierz72.setText(resourceMap.getString("macierz72.text")); // NOI18N
        macierz72.setName("macierz72"); // NOI18N

        macierz71.setText(resourceMap.getString("macierz71.text")); // NOI18N
        macierz71.setName("macierz71"); // NOI18N

        macierz70.setText(resourceMap.getString("macierz70.text")); // NOI18N
        macierz70.setName("macierz70"); // NOI18N

        macierz88.setText(resourceMap.getString("macierz88.text")); // NOI18N
        macierz88.setName("macierz88"); // NOI18N

        macierz87.setText(resourceMap.getString("macierz87.text")); // NOI18N
        macierz87.setName("macierz87"); // NOI18N

        macierz86.setText(resourceMap.getString("macierz86.text")); // NOI18N
        macierz86.setName("macierz86"); // NOI18N

        macierz85.setText(resourceMap.getString("macierz85.text")); // NOI18N
        macierz85.setName("macierz85"); // NOI18N

        macierz84.setText(resourceMap.getString("macierz84.text")); // NOI18N
        macierz84.setName("macierz84"); // NOI18N

        macierz83.setText(resourceMap.getString("macierz83.text")); // NOI18N
        macierz83.setName("macierz83"); // NOI18N

        macierz82.setText(resourceMap.getString("macierz82.text")); // NOI18N
        macierz82.setName("macierz82"); // NOI18N

        macierz80.setText(resourceMap.getString("macierz80.text")); // NOI18N
        macierz80.setName("macierz80"); // NOI18N

        macierz81.setText(resourceMap.getString("macierz81.text")); // NOI18N
        macierz81.setName("macierz81"); // NOI18N

        javax.swing.GroupLayout panelMacierzyLayout = new javax.swing.GroupLayout(panelMacierzy);
        panelMacierzy.setLayout(panelMacierzyLayout);
        panelMacierzyLayout.setHorizontalGroup(
            panelMacierzyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMacierzyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelMacierzyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMacierzyLayout.createSequentialGroup()
                        .addComponent(macierz00, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz01, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz02, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz03, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz04, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz05, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz06, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz07, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz08, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMacierzyLayout.createSequentialGroup()
                        .addComponent(macierz10, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz12, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz13, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz14, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz15, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz16, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz17, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz18, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMacierzyLayout.createSequentialGroup()
                        .addComponent(macierz20, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz21, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz22, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz23, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz24, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz25, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz26, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz27, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz28, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMacierzyLayout.createSequentialGroup()
                        .addComponent(macierz30, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz31, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz32, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz33, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz34, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz35, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz36, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz37, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz38, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMacierzyLayout.createSequentialGroup()
                        .addComponent(macierz40, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz41, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz42, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz43, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz44, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz45, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz46, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz47, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz48, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMacierzyLayout.createSequentialGroup()
                        .addComponent(macierz50, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz51, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz52, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz53, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz54, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz55, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz56, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz57, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz58, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMacierzyLayout.createSequentialGroup()
                        .addComponent(macierz60, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz61, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz62, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz63, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz64, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz65, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz66, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz67, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz68, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMacierzyLayout.createSequentialGroup()
                        .addComponent(macierz70, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz71, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz72, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz73, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz74, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz75, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz76, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz77, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz78, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMacierzyLayout.createSequentialGroup()
                        .addComponent(macierz81, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz80, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz82, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz83, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz84, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz85, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz86, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz87, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(macierz88, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelMacierzyLayout.setVerticalGroup(
            panelMacierzyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMacierzyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMacierzyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(macierz00, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz03, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz04, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz05, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz06, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz07, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz08, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMacierzyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(macierz10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMacierzyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(macierz20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMacierzyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(macierz30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMacierzyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(macierz40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz48, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMacierzyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(macierz50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz51, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz52, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz53, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz54, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz55, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz56, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz57, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz58, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMacierzyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(macierz60, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz61, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz62, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz63, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz64, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz65, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz66, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz67, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz68, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMacierzyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(macierz70, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz71, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz72, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz73, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz74, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz75, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz76, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz77, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz78, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMacierzyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(macierz81, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz80, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz82, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz83, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz84, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz85, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz86, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz87, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(macierz88, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelMacierzy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rozmiarXSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rozmiarYSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(rozmiarXSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(rozmiarYSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelMacierzy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(272, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
            .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

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
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

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

        wlasny.setText(resourceMap.getString("wlasny.text")); // NOI18N
        wlasny.setName("wlasny"); // NOI18N
        wlasny.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wlasnyActionPerformed(evt);
            }
        });
        jMenu2.add(wlasny);

        menuBar.add(jMenu2);

        jMenu3.setText(resourceMap.getString("jMenu3.text")); // NOI18N
        jMenu3.setName("jMenu3"); // NOI18N

        erozja.setText(resourceMap.getString("erozja.text")); // NOI18N
        erozja.setName("erozja"); // NOI18N
        erozja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                erozjaActionPerformed(evt);
            }
        });
        jMenu3.add(erozja);

        dylatacja.setText(resourceMap.getString("dylatacja.text")); // NOI18N
        dylatacja.setName("dylatacja"); // NOI18N
        dylatacja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dylatacjaActionPerformed(evt);
            }
        });
        jMenu3.add(dylatacja);

        otwarcie.setText(resourceMap.getString("otwarcie.text")); // NOI18N
        otwarcie.setName("otwarcie"); // NOI18N
        otwarcie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                otwarcieActionPerformed(evt);
            }
        });
        jMenu3.add(otwarcie);

        zamkniecie.setText(resourceMap.getString("zamkniecie.text")); // NOI18N
        zamkniecie.setName("zamkniecie"); // NOI18N
        zamkniecie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zamkniecieActionPerformed(evt);
            }
        });
        jMenu3.add(zamkniecie);

        menuBar.add(jMenu3);

        jMenu4.setText(resourceMap.getString("jMenu4.text")); // NOI18N
        jMenu4.setName("jMenu4"); // NOI18N

        wyrownanie.setText(resourceMap.getString("wyrownanie.text")); // NOI18N
        wyrownanie.setName("wyrownanie"); // NOI18N
        wyrownanie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wyrownanieActionPerformed(evt);
            }
        });
        jMenu4.add(wyrownanie);

        binaryzacja.setText(resourceMap.getString("binaryzacja.text")); // NOI18N
        binaryzacja.setName("binaryzacja"); // NOI18N
        binaryzacja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                binaryzacjaActionPerformed(evt);
            }
        });
        jMenu4.add(binaryzacja);

        obliczHistogram.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        obliczHistogram.setText(resourceMap.getString("obliczHistogram.text")); // NOI18N
        obliczHistogram.setName("obliczHistogram"); // NOI18N
        obliczHistogram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                obliczHistogramActionPerformed(evt);
            }
        });
        jMenu4.add(obliczHistogram);

        menuBar.add(jMenu4);

        jMenu5.setText(resourceMap.getString("jMenu5.text")); // NOI18N
        jMenu5.setName("jMenu5"); // NOI18N

        dodajobraz.setText(resourceMap.getString("dodajobraz.text")); // NOI18N
        dodajobraz.setName("dodajobraz"); // NOI18N
        dodajobraz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dodajobrazActionPerformed(evt);
            }
        });
        jMenu5.add(dodajobraz);

        odejmijobraz.setText(resourceMap.getString("odejmijobraz.text")); // NOI18N
        odejmijobraz.setName("odejmijobraz"); // NOI18N
        odejmijobraz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                odejmijobrazActionPerformed(evt);
            }
        });
        jMenu5.add(odejmijobraz);

        menuBar.add(jMenu5);

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
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 655, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 471, Short.MAX_VALUE)
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
        plotno.Splot(matrix, rozmiar, rozmiar);
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
        plotno.Splot(matrix, rozmiar, rozmiar);
    }//GEN-LAST:event_laplacianActionPerformed

    private void usredniajacyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usredniajacyActionPerformed
        // http://www.projektydoszkoly.za.pl/index.html#dolno
        float[] matrix = {
            1/9f,1/9f,1/9f,
            1/9f,1/9f,1/9f,
            1/9f,1/9f,1/9f
        };
        int rozmiar = 3;
        plotno.Splot(matrix, rozmiar, rozmiar);
    }//GEN-LAST:event_usredniajacyActionPerformed

    private void lp2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lp2ActionPerformed
        // http://www.projektydoszkoly.za.pl/index.html#dolno
        float[] matrix = {
            1/16f,1/16f,1/16f,
            1/16f,4/16f,1/16f,
            1/16f,1/16f,1/16f
        };
        int rozmiar = 3;
        plotno.Splot(matrix, rozmiar, rozmiar);
    }//GEN-LAST:event_lp2ActionPerformed

    private void gorny1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gorny1ActionPerformed
        // http://www.projektydoszkoly.za.pl/index.html#dolno
        float[] matrix = {
            -1f,-1f,-1f,
            -1f,9f,-1f,
            -1f,-1f,-1f
        };
        int rozmiar = 3;
        plotno.Splot(matrix, rozmiar, rozmiar);
    }//GEN-LAST:event_gorny1ActionPerformed

    private void gorny2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gorny2ActionPerformed
        // http://www.projektydoszkoly.za.pl/index.html#dolno
        float[] matrix = {
            1f,-2f,1f,
            -2f,5f,-2f,
            1f,-2f,1f
        };
        int rozmiar = 3;
        plotno.Splot(matrix, rozmiar, rozmiar);
    }//GEN-LAST:event_gorny2ActionPerformed

    private void gorny3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gorny3ActionPerformed
        // http://www.projektydoszkoly.za.pl/index.html#dolno
        float[] matrix = {
            0f,-1/16f,0f,
            -1/16f,20/16f,-1/16f,
            0f,-1/16f,0f
        };
        int rozmiar = 3;
        plotno.Splot(matrix, rozmiar, rozmiar);
    }//GEN-LAST:event_gorny3ActionPerformed

    private void krawedziePoziomyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_krawedziePoziomyActionPerformed
        // http://www.projektydoszkoly.za.pl/index.html#dolno
        float[] matrix = {
            0f,-1f,0f,
            0f,1f,0f,
            0f,0f,0f
        };
        int rozmiar = 3;
        plotno.Splot(matrix, rozmiar, rozmiar);
    }//GEN-LAST:event_krawedziePoziomyActionPerformed

    private void krawedziePionowyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_krawedziePionowyActionPerformed
        // http://www.projektydoszkoly.za.pl/index.html#dolno
        float[] matrix = {
            0f,0f,0f,
            -1f,1f,0f,
            0f,0f,0f
        };
        int rozmiar = 3;
        plotno.Splot(matrix, rozmiar, rozmiar);
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
        plotno.Splot(matrix, rozmiar, rozmiar);
    }//GEN-LAST:event_stiffnessActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu2ActionPerformed
    private void wlasnyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wlasnyActionPerformed
        pobierzMacierz();
        for(int i=0; i<macierz.length;i++)
            System.out.print(macierz[i]+ ", ");
        plotno.Splot(macierz, rozmiarX, rozmiarY);
    }//GEN-LAST:event_wlasnyActionPerformed

    private void obliczHistogramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_obliczHistogramActionPerformed
        plotno.ObliczHistogram();
        histogramPanel.DrawHistogram(plotno.histogram, Color.black);
        histogramPanelR.DrawHistogram(plotno.histogramR, Color.red);
        histogramPanelG.DrawHistogram(plotno.histogramG, Color.green);
        histogramPanelB.DrawHistogram(plotno.histogramB, Color.blue);
    }//GEN-LAST:event_obliczHistogramActionPerformed

    private void wyrownanieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wyrownanieActionPerformed
        plotno.wyrownanie();
    }//GEN-LAST:event_wyrownanieActionPerformed

    private void binaryzacjaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_binaryzacjaActionPerformed
        if(this.pobierzWartosc()){
            plotno.Binaryzacja(wartosc);
        }
    }//GEN-LAST:event_binaryzacjaActionPerformed

    private void erozjaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_erozjaActionPerformed
        // http://atol.am.gdynia.pl/tc/Radzienski/morfologiczne.htm
        pobierzMacierz();
        plotno.Erozja(macierz, rozmiarX, rozmiarY);
    }//GEN-LAST:event_erozjaActionPerformed

    private void dylatacjaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dylatacjaActionPerformed
        pobierzMacierz();
        plotno.Dylatacja(macierz, rozmiarX, rozmiarY);
    }//GEN-LAST:event_dylatacjaActionPerformed

    private void otwarcieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_otwarcieActionPerformed
        pobierzMacierz();
        plotno.Erozja(macierz, rozmiarX, rozmiarY);
        plotno.Dylatacja(macierz, rozmiarX, rozmiarY);
    }//GEN-LAST:event_otwarcieActionPerformed

    private void zamkniecieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zamkniecieActionPerformed
        pobierzMacierz();
        plotno.Dylatacja(macierz, rozmiarX, rozmiarY);
        plotno.Erozja(macierz, rozmiarX, rozmiarY);
    }//GEN-LAST:event_zamkniecieActionPerformed

    private void rozmiarXSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rozmiarXSpinnerStateChanged
        ustawInputy();
    }//GEN-LAST:event_rozmiarXSpinnerStateChanged

    private void rozmiarYSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rozmiarYSpinnerStateChanged
        ustawInputy();
    }//GEN-LAST:event_rozmiarYSpinnerStateChanged

    private void dodajobrazActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dodajobrazActionPerformed
        try {
            plotno.OtworzDrugi();
        } catch (IOException ex) {
            Logger.getLogger(SelerShopView.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(plotno.drugiplik != null){
            plotno.DwaObrazy(true); // true to dodawanie, false - odejmowanie
        }
    }//GEN-LAST:event_dodajobrazActionPerformed

    private void odejmijobrazActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_odejmijobrazActionPerformed
        try {
            plotno.OtworzDrugi();
        } catch (IOException ex) {
            Logger.getLogger(SelerShopView.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(plotno.drugiplik != null){
            plotno.DwaObrazy(false); // true to dodawanie, false - odejmowanie
        }
    }//GEN-LAST:event_odejmijobrazActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem binaryzacja;
    private javax.swing.JMenuItem dodajobraz;
    private javax.swing.JMenuItem dodawanie;
    private javax.swing.JMenuItem dylatacja;
    private javax.swing.JMenuItem erozja;
    private javax.swing.JMenuItem gauss;
    private javax.swing.JMenuItem gorny1;
    private javax.swing.JMenuItem gorny2;
    private javax.swing.JMenuItem gorny3;
    private myprzegladarka.histogramPanel histogramPanel;
    private myprzegladarka.histogramPanel histogramPanelB;
    private myprzegladarka.histogramPanel histogramPanelG;
    private myprzegladarka.histogramPanel histogramPanelR;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JMenuItem krawedziePionowy;
    private javax.swing.JMenuItem krawedziePoziomy;
    private javax.swing.JMenuItem laplacian;
    private javax.swing.JMenuItem lp2;
    private javax.swing.JTextField macierz00;
    private javax.swing.JTextField macierz01;
    private javax.swing.JTextField macierz02;
    private javax.swing.JTextField macierz03;
    private javax.swing.JTextField macierz04;
    private javax.swing.JTextField macierz05;
    private javax.swing.JTextField macierz06;
    private javax.swing.JTextField macierz07;
    private javax.swing.JTextField macierz08;
    private javax.swing.JTextField macierz10;
    private javax.swing.JTextField macierz11;
    private javax.swing.JTextField macierz12;
    private javax.swing.JTextField macierz13;
    private javax.swing.JTextField macierz14;
    private javax.swing.JTextField macierz15;
    private javax.swing.JTextField macierz16;
    private javax.swing.JTextField macierz17;
    private javax.swing.JTextField macierz18;
    private javax.swing.JTextField macierz20;
    private javax.swing.JTextField macierz21;
    private javax.swing.JTextField macierz22;
    private javax.swing.JTextField macierz23;
    private javax.swing.JTextField macierz24;
    private javax.swing.JTextField macierz25;
    private javax.swing.JTextField macierz26;
    private javax.swing.JTextField macierz27;
    private javax.swing.JTextField macierz28;
    private javax.swing.JTextField macierz30;
    private javax.swing.JTextField macierz31;
    private javax.swing.JTextField macierz32;
    private javax.swing.JTextField macierz33;
    private javax.swing.JTextField macierz34;
    private javax.swing.JTextField macierz35;
    private javax.swing.JTextField macierz36;
    private javax.swing.JTextField macierz37;
    private javax.swing.JTextField macierz38;
    private javax.swing.JTextField macierz40;
    private javax.swing.JTextField macierz41;
    private javax.swing.JTextField macierz42;
    private javax.swing.JTextField macierz43;
    private javax.swing.JTextField macierz44;
    private javax.swing.JTextField macierz45;
    private javax.swing.JTextField macierz46;
    private javax.swing.JTextField macierz47;
    private javax.swing.JTextField macierz48;
    private javax.swing.JTextField macierz50;
    private javax.swing.JTextField macierz51;
    private javax.swing.JTextField macierz52;
    private javax.swing.JTextField macierz53;
    private javax.swing.JTextField macierz54;
    private javax.swing.JTextField macierz55;
    private javax.swing.JTextField macierz56;
    private javax.swing.JTextField macierz57;
    private javax.swing.JTextField macierz58;
    private javax.swing.JTextField macierz60;
    private javax.swing.JTextField macierz61;
    private javax.swing.JTextField macierz62;
    private javax.swing.JTextField macierz63;
    private javax.swing.JTextField macierz64;
    private javax.swing.JTextField macierz65;
    private javax.swing.JTextField macierz66;
    private javax.swing.JTextField macierz67;
    private javax.swing.JTextField macierz68;
    private javax.swing.JTextField macierz70;
    private javax.swing.JTextField macierz71;
    private javax.swing.JTextField macierz72;
    private javax.swing.JTextField macierz73;
    private javax.swing.JTextField macierz74;
    private javax.swing.JTextField macierz75;
    private javax.swing.JTextField macierz76;
    private javax.swing.JTextField macierz77;
    private javax.swing.JTextField macierz78;
    private javax.swing.JTextField macierz80;
    private javax.swing.JTextField macierz81;
    private javax.swing.JTextField macierz82;
    private javax.swing.JTextField macierz83;
    private javax.swing.JTextField macierz84;
    private javax.swing.JTextField macierz85;
    private javax.swing.JTextField macierz86;
    private javax.swing.JTextField macierz87;
    private javax.swing.JTextField macierz88;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem mnozenie;
    private javax.swing.JMenuItem obliczHistogram;
    private javax.swing.JMenuItem odejmijobraz;
    private javax.swing.JMenuItem otwarcie;
    private javax.swing.JMenuItem otworz;
    private javax.swing.JPanel panelMacierzy;
    private javax.swing.JPanel panelnahistogramy;
    private selershop.Plotno plotno;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenuItem resetuj;
    private javax.swing.JSpinner rozmiarXSpinner;
    private javax.swing.JSpinner rozmiarYSpinner;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JMenuItem stiffness;
    private javax.swing.JMenuItem usredniajacy;
    private javax.swing.JMenuItem wlasny;
    private javax.swing.JMenuItem wyrownanie;
    private javax.swing.JMenuItem zamkniecie;
    private javax.swing.JMenuItem zapisz;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
    private Float wartosc;
    
    float[] macierz;
    int rozmiarX;
    int rozmiarY;
    
}
