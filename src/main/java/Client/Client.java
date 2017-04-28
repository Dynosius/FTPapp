//TODO: ponovo napraviti login exception handling -> nullpointerexception u slucaju fail logina

package Client;
import org.apache.commons.net.ftp.*;
import java.io.*;
import java.util.HashMap;


/**
 * Created by dkralj on 13.4.2017.
 */
public class Client extends Properties
{
    private FTPClient ftp = new FTPClient();
    private final static int bsize = 2048;
    private double speed;
    private HashMap hm;


    public Client(String user, String pass)
    {
        super(user, pass);
        ConnectToService();
        login(user, pass);
    }

    private void ConnectToService()
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
    }

    protected String[] Listfiles(){
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
        ///Katastrofa!!
    public void DownloadFile(String fileName)
    {
        //Otvaranje threada
        new Thread(() ->
        {
            DwnldWindow progressBar = new DwnldWindow();
            progressBar.setVisible(true);
            FileOutputStream outputStream = null;
            BufferedInputStream buffIn = null;
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
                    if ((count = buffIn.read(data)) != -1 && data != null) {
                        //TODO: veliki problem, filovi imaju veci size nego prije
                        //Ima veze s velicinom data-e (bsize), ako stavim bsize = 2, ispadne sve ok, sa 1024+ fileovi su veci od originala
                        outputStream.write(data, 0, count);
                        downloaded += data.length;
                        progressBar.setPBValue((int) (downloaded / fileSize * 100));
                        progressBar.refresh();
                    } else {
                        break;
                    }
                }
                endTime = System.currentTimeMillis();
                //Bitno!
                ftpStream.close();
                ftp.completePendingCommand();
                buffIn.close();
                if (outputStream != null)
                {
                    outputStream.close();
                }
                ////////////////////////////

                fileSize /= 1024f;
                time = (endTime - startTime) / 1000f;  //time in seconds
                if (time == 0) time = 1;
                this.speed = fileSize / time;
                System.out.printf("Speed is %.2f KBps\n", this.speed);

            } catch (IOException e) {
                System.err.println("Input stream failed to open");
                e.printStackTrace();
            }
        }).start();
    }

    protected boolean login(String name, String pass)
    {
        boolean loginStatus = false;
        try {
            loginStatus = ftp.login(name, pass);
        } catch (IOException e) {
            System.err.println("Failed to authorize!!");
            System.out.println(ftp.getReplyString());
            e.printStackTrace();
        }
        return loginStatus;
    }
}
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //      OLD VERSION!!

       /* public void UploadFile(String filePath)
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
        }*/

    /*public void DownloadFile(String fileName, Consumer<Integer> c)
    {
        //TODO:Napisi fileoutputstream za monitoranje byteova -> up/down speed
        new Thread(() ->
        {
            FileOutputStream OutputFile = null;
            try {
                OutputFile = new FileOutputStream("C:\\Users\\dkralj\\Desktop\\downloads\\" + fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                //ftp.retrieveFile ("/" + fileName, OutputFile);
                //petlja koja čita preuzete byteove
//                c.accept(bytesDownload);
                System.out.println(ftp.getReplyString());
                OutputFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }*/

    /*
         //Redundant as of yet, disconnects after exiting app automatically
         public void Disconnect ()
        {
            try {
                ftp.disconnect();
                System.out.println("Succesfully disconnected.");
            } catch (IOException e) {
                System.err.println ("Failed to disconnect");
                e.printStackTrace();
            }
        }

        */

