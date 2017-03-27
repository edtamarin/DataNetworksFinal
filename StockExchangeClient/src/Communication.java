import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Egor Tamarin on 27-Mar-17.
 */
public class Communication {
    private String address;
    private int port;
    public Communication(String addr, int po){
        this.address = addr;
        this.port = po;
    }
    public String sendMessage(Message m) throws Exception{
        Socket commSocket = new Socket(addr,port);
        DataOutputStream outToServer = new DataOutputStream(commSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(
                new InputStreamReader(commSocket.getInputStream()));
        outToServer.writeBytes(m.getMsgToSend());
        response = inFromServer.readLine();
    }
}
