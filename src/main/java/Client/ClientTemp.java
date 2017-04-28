package Client;

import java.io.*;
import java.net.*;
/**
 * Created by dkralj on 27.4.2017.
 */
public class ClientTemp extends Properties
{
    String hostName = getServerAddress();
    int portNumber = 14147;

    public ClientTemp()
    {
        try
        {
            Socket socket = new Socket (hostName, portNumber);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
