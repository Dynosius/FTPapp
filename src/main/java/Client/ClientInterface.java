package Client;

/**
 * Created by dkralj on 4.5.2017.
 */
public interface ClientInterface {

    public void ConnectToService();
    public void UploadFile(String filePath);
    public String[] Listfiles();
    public void DownloadFile(String fileName);
    public boolean login(String name, String pass);
}
