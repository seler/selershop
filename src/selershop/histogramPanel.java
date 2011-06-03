
package myprzegladarka;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
/**
 *
 * @author wojciech
 */
public class histogramPanel extends javax.swing.JPanel {

    int[] histogram;
    Color histColor;
    
    public histogramPanel() {
        initComponents();
        histogram = new int[256];
        for(int i = 0; i<256; i++)
            histogram[i] = 0;
        histColor = Color.BLACK;
    }


    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 256, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 146, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    public void DrawHistogram(int[] histogram, Color color)
    {
        histColor = color;
        this.histogram = histogram;
        repaint();
    }


    @Override
    public void paint(Graphics g)
    {
        Graphics2D g2D = (Graphics2D)g;
        int max = 1;

        for(int i = 0; i < 256; i++)
            if(histogram[i] > max)
                max = histogram[i];
        g2D.clearRect(0, 0, this.getWidth(), this.getHeight());
        g2D.setColor(Color.GRAY);
        g2D.setStroke(new BasicStroke(2));
        g2D.drawLine(0, 104, 0, 4);
        g2D.drawLine(0, 104, 256, 104);

        g2D.setStroke(new BasicStroke(1));
        g2D.setColor(histColor);
        for(int i = 0; i < 256; i++)
        {
            g2D.drawLine(2+i, 104, 2+i, 104 - (int)((float)histogram[i]/(float)max * 100));
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
