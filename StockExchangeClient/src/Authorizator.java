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

    public String getLogin() { // getter
        return login;
    }

    public boolean authorizeUser(String addr, int port) throws Exception{ // authorizing
        ArrayList<String> response;
        Message authMsg = new Message();
        Communication authComm = new Communication(addr,port); // open TCP connection
        authMsg.authMessage(this.login,this.password); // create an auth message
        authComm.sendMessage(authMsg); // send it
        response = authComm.receiveMessage(); // receive response
        authComm.terminate(); // close connection
        if (authMsg.analyzeMessage(response) == 211){ // if response code OK
            return true; // auth user
        }else{ // if not
            return false; // tell user to try again.
        }
    }
}
