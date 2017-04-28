package Client;

import javax.swing.*;
import java.awt.*;

/**
 * Created by dkralj on 21.4.2017.
 */
public class DwnldWindow{
    JFrame theFrame;
    private final JProgressBar progressBar = new JProgressBar();
    public DwnldWindow()
    {
        theFrame = new JFrame("Downloading");
        theFrame.setResizable(false);
        //theFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        progressBar.setValue(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true);
        progressBar.setBorder(BorderFactory.createTitledBorder("Download File"));
        Container contentPane = theFrame.getContentPane();
        contentPane.add(progressBar, BorderLayout.SOUTH);
        theFrame.setSize(300, 150);
        theFrame.setLocationRelativeTo(null);
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
        this.progressBar.repaint();
    }
}
