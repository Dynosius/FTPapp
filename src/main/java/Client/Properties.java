package Client;

/**
 * Created by dkralj on 13.4.2017.
 */
public class Properties
{
    private String serverAddress;
    private String userID;
    private String password;

    public Properties ()
    {
        this.serverAddress = "127.0.0.1";
        this.userID = "Dino";
        this.password = "Kralj";
    }


    public String getServerAddress ()
    {
        return this.serverAddress;
    }
    public String getUserID ()
    {
        return this.userID;
    }
    public String getPassword ()
    {
        return this.password;
    }
    public void setUserID (String ID)
    {
        this.userID = ID;
    }
    public void setPassword (String password)
    {
        this.password = password;
    }
    public void setServerAddress (String Address) { this.serverAddress = Address; }

}
