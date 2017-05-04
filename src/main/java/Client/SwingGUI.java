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
    private JButton uploadBTN, downloadBTN, changeSRV;
    private JLabel statusText;
    private JFileChooser jfc;
    private JList<String> lista;
    private String fileName;
    private String username, password, server = "127.0.0.1";


    public SwingGUI ()  //Initialize Login pop up windows
    {
        super("DinoZilla FTP client");
        this.username = JOptionPane.showInputDialog("Enter user:");
        JPasswordField pass = new JPasswordField();
        JOptionPane.showConfirmDialog(null, pass,"Options", JOptionPane.OK_CANCEL_OPTION);
        char[] arr = pass.getPassword();
        this.password = String.valueOf(arr);
        //instanciranje objekta samo za login, losa optimizacija jer ne trebam taj objekt nizasto osim za login checkup
        Client login = new Client(username, password, server);
        if(login.login(username, password))
        {
            init();
            login = null;
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Login failed");
            System.exit(-1);
        }
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
        changeSRV = new JButton("Change Server");
        statusText = new JLabel("");
        jfc = new JFileChooser();

        //actionlistener
        uploadBTN.addActionListener(this);
        downloadBTN.addActionListener(this);
        changeSRV.addActionListener(this);

        //adding
        panel1.add(uploadBTN);
        panel1.add(downloadBTN);
        panel1.add(changeSRV);
        panel2.add(statusText);
        add(panel1);
        add(panel2);
    }

    public void actionPerformed(ActionEvent e)
    {
        Object obj = e.getSource();
        if (obj == uploadBTN)
        {
            Client clientInstance = new Client(username, password, server);
            jfc.showOpenDialog(this);
            clientInstance.UploadFile(jfc.getSelectedFile().getAbsolutePath());
            statusText.setText("Uploaded file");
        }
        else if (obj == downloadBTN)
        {
            Client clientInstance = new Client(username, password, server);
            String[] names = clientInstance.Listfiles();
            JList<String> lokalnaLista = new JList<>(names);
            this.lista = lokalnaLista;
            lista.addListSelectionListener(this);

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
        else if (obj == changeSRV)
        {
            this.server = JOptionPane.showInputDialog("Enter server address:");
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
