package Client;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by dkralj on 20.4.2017.
 */
public class SwingGUI extends JFrame implements ActionListener, ListSelectionListener {
    private JButton uploadBTN, downloadBTN;
    private JLabel statusText;
    private JFileChooser jfc;
    private JList<String> lista;
    private String fileName;
    private String username, password;


    public SwingGUI ()  //Initialize Login pop up windows
    {
        super("DinoZilla FTP client");
        //clientInstance.ConnectToService();
        this.username = JOptionPane.showInputDialog("Enter user:");
        JPasswordField pass = new JPasswordField();
        JOptionPane.showConfirmDialog(null, pass,"Options", JOptionPane.OK_CANCEL_OPTION);
        char[] arr = pass.getPassword();
        this.password = String.valueOf(arr);

        init();
    }

    private void init()     //Initialize Swing GUI
    {
        //Window properties
        setSize (400, 200);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(0,1));
        //components
        JPanel panel1 = new JPanel(new FlowLayout());
        JPanel panel2 = new JPanel(new FlowLayout());
        uploadBTN = new JButton("Upload");
        downloadBTN = new JButton ("Download");
        statusText = new JLabel("");
        jfc = new JFileChooser();

        //actionlistener
        uploadBTN.addActionListener(this);
        downloadBTN.addActionListener(this);

        //adding
        panel1.add(uploadBTN);
        panel1.add(downloadBTN);
        panel2.add(statusText);
        add(panel1);
        add(panel2);
    }

    public void actionPerformed(ActionEvent e)
    {
        Object obj = e.getSource();
        if (obj == uploadBTN)
        {
            Client clientInstance = new Client(username, password);
            jfc.showOpenDialog(this);
            clientInstance.UploadFile(jfc.getSelectedFile().getAbsolutePath());
            statusText.setText("Uploaded file");
        }
        else if (obj == downloadBTN)
        {
            Client clientInstance = new Client(username, password);
            String[] names = clientInstance.Listfiles();;
            if (this.lista == null)
            {
                this.lista = new JList<>(names);
                lista.addListSelectionListener(this);
            }

            int n = JOptionPane.showConfirmDialog(null, lista, "Options:", JOptionPane.OK_CANCEL_OPTION);
            if (n == JOptionPane.OK_OPTION)
            {
                clientInstance.DownloadFile(fileName);
                if (fileName != null) {
                    statusText.setText("Downloaded file " + fileName);
                } else statusText.setText("Failed to download file");
            }
            else
            {
                System.out.println("Cancelled");
                statusText.setText("Cancelled");
            }
        }
    }

    public void valueChanged(ListSelectionEvent a)
    {
        if (!a.getValueIsAdjusting())
        {
            this.fileName = lista.getSelectedValue();
        }
    }
}
