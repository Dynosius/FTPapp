package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by dkralj on 21.4.2017.
 */
public class DwnldWindow implements ActionListener{
    JFrame theFrame;
    private final JProgressBar progressBar = new JProgressBar();
    private JButton botun;
    private boolean cancel = false;
    public DwnldWindow()
    {
        theFrame = new JFrame("Downloading");
        theFrame.setResizable(false);
        botun = new JButton("Cancel");
        progressBar.setValue(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true);
        progressBar.setBorder(BorderFactory.createTitledBorder("Download File"));
        Container contentPane = theFrame.getContentPane();
        contentPane.setLayout(new GridLayout(0, 1));
        contentPane.add(progressBar, 0, 0);
        contentPane.add(botun, 0, 1);
        theFrame.setSize(300, 150);
        theFrame.setLocationRelativeTo(null);
        botun.addActionListener(this);
    }

    public void setPBValue(int x)
    {
        progressBar.setValue(x);
    }
    public void setVisible(boolean info)
    {
        this.theFrame.setVisible(info);
    }
    public void refresh ()
    {
        if(progressBar.getValue() >= 100)
        {
            botun.setText("Close");
        }
        this.progressBar.repaint();
    }
    public boolean isCancel() { return this.cancel;}
    public void close()
    {
        theFrame.setVisible(false);
        theFrame.dispose();
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        cancel = true;
        if(botun.getText().equals("Close")) close();
    }
}
