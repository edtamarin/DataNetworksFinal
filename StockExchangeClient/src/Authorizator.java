import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

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
        ArrayList<String> response;
        Message authMsg = new Message();
        Communication authComm = new Communication(addr,port);
        authMsg.authMessage(this.login,this.password);
        authComm.sendMessage(authMsg);
        response = authComm.receiveMessage();
        if (authMsg.analyzeMessage(response) == 211){
            return true;
        }else{
            return false;
        }
    }
}
