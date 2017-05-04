//http://stackoverflow.com/questions/8982420/time-out-method-in-java -> za kada login na server nije uspjesan pa baca 100 errora za swing
//^rijeseno sa system exit, moguca dodatna implementacija
//TODO: napraviti upload isto kao i download
package Client;
import org.apache.commons.net.ftp.*;

import javax.swing.*;
import java.io.*;
import java.util.HashMap;


/**
 * Created by dkralj on 13.4.2017.
 */
public class Client extends Properties implements ClientInterface
{
    private FTPClient ftp = new FTPClient();
    private final static int bsize = 2048;
    private double speed;
    private HashMap hm;

    Client(String user, String pass, String server)
    {
        super(user, pass, server);
        ConnectToService();
        login(user, pass);
    }

    public void ConnectToService()
    {
        try
        {
            ftp.connect (getServerAddress());
            ftp.enterLocalPassiveMode();
            ftp.setFileType(ftp.BINARY_FILE_TYPE, ftp.BINARY_FILE_TYPE);
            if (ftp.isConnected())
            {
                System.out.println("Connected to " + getServerAddress());
            }
        }
        catch (IOException e)
        {
            if(ftp.isConnected())
            {
                try
                {
                    ftp.disconnect();
                }
                catch (IOException e1)
                {
                    System.err.println ("Failed to disconnect??");
                }
            }
            e.printStackTrace();
        }
    }

    public void Disconnect()
    {
        try {
            ftp.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void UploadFile(String filePath)
    {
        try
        {
            File file = new File(filePath);
            String name = file.getName();
            FileInputStream InputFile = new FileInputStream(filePath);
            ftp.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            ftp.storeFile(name, InputFile);
            System.out.println(ftp.getReplyString());
            InputFile.close();
        }
        catch (FileNotFoundException e)
        {
            System.err.println ("Could not open file");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Disconnect();
    }

    public String[] Listfiles(){
        try {
                hm = new HashMap();
                for (FTPFile ftpFile : ftp.listFiles())
                {
                    hm.put(ftpFile.getName(), (double)ftpFile.getSize());
                }
            return ftp.listNames();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void DownloadFile(String fileName)
    {
        new Thread(() ->
        {
            DwnldWindow progressBar = new DwnldWindow();
            progressBar.setVisible(true);
            FileOutputStream outputStream = null;
            BufferedInputStream buffIn;
            byte[] data = new byte[bsize];
            long startTime, endTime, downloaded = 0;
            double time, fileSize = (double) hm.get(fileName);
            int count = 0;

            try {
                outputStream = new FileOutputStream("C:\\Users\\dkralj\\Desktop\\downloads\\" + fileName);
            } catch (FileNotFoundException e) {
                System.err.println("Output stream error");
                e.printStackTrace();
            }
            try {
                InputStream ftpStream = ftp.retrieveFileStream(fileName);
                buffIn = new BufferedInputStream(ftpStream, bsize);

                //petlja za write/DL speed kalkulaciju
                startTime = System.currentTimeMillis();
                while (true) {

                    //Ako se pritisne cancel button, brise se file i zatvara prozor
                    if(progressBar.isCancel())
                    {
                        cleanUp(ftpStream, ftp, buffIn, outputStream);
                        deleteFile("C:\\Users\\dkralj\\Desktop\\downloads\\" + fileName);
                        progressBar.close();
                        break;
                    }

                    //Progress bar i prebacivanje byteova
                    if ((count = buffIn.read(data)) != -1 && data != null) {
                        outputStream.write(data, 0, count);
                        downloaded += count;
                        progressBar.setPBValue((int) (downloaded / fileSize * 100));    //Percentage of the download
                        progressBar.refresh();
                    } else {
                        break;
                    }
                }
                endTime = System.currentTimeMillis();
                cleanUp(ftpStream, ftp, buffIn, outputStream);

                fileSize /= 1024f;
                time = (endTime - startTime) / 1000f;  //time in seconds
                if (time == 0) time = 1;
                this.speed = fileSize / time;
                System.out.printf("Speed is %.2f KBps\n", this.speed);

            } catch (IOException e) {
                System.err.println("Input stream failed to open");
                e.printStackTrace();
            }
            Disconnect();
        }).start();
    }

    public boolean login(String name, String pass)
    {
        boolean loginStatus = false;
        try {
            loginStatus = ftp.login(name, pass);
        } catch (IOException e) {
            System.err.println("Failed to authorize!!");
            System.out.println(ftp.getReplyString());
            e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(), "Error, could not authorize!");
            System.exit(-1);
        }
        return loginStatus;
    }

    private boolean deleteFile(String fileName)
    {
        File file = new File(fileName);
        boolean delete = file.delete();
        return delete;
    }
    //Zatvara sve streamove
    private void cleanUp(InputStream ftpStream, FTPClient ftp, BufferedInputStream buffIn, FileOutputStream outputStream) throws IOException
    {
        ftpStream.close();
        ftp.completePendingCommand();
        buffIn.close();
        if (outputStream != null)
        {
            outputStream.close();
        }
    }
}

