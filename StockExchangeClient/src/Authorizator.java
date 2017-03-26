import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Egor Tamarin on 26-Mar-17.
 * This class handles the authorization procedure
 */
public class Authorizator {
    private String login; // user has to pass the credentials
    private String password;
    public Authorizator(String lo, String pw){
        this.login = lo;
        this.password = pw;
    }
    public boolean authorizeUser(String addr, int port) throws Exception{
        String response;
        Message authMsg = new Message();
        Socket authSocket = new Socket(addr,port);
        DataOutputStream outToServer = new DataOutputStream(authSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(
                new InputStreamReader(authSocket.getInputStream()));
        outToServer.writeBytes(authMsg.authMessage(this.login,this.password));
        response = inFromServer.readLine();
        if (response == ""){
            return true;
        }else{
            return false;
        }
    }
}
