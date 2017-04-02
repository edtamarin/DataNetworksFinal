import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Egor Tamarin on 27-Mar-17.
 */
public class Communication {
    private String address;
    private int port;
    private Socket commSocket;
    public Communication(){

    }
    public Communication(String addr, int po) throws Exception{
        this.address = addr;
        this.port = po;
        this.commSocket = new Socket(this.address,this.port);
    }
    public void createSocket(String addr, int po) throws Exception{
        this.commSocket = new Socket(addr,po);
    }
    public void sendMessage(Message m) throws Exception{ // sends what is fed to the method
        DataOutputStream outToServer = new DataOutputStream(commSocket.getOutputStream());
        outToServer.writeBytes(m.getMsgToSend());
    }
    public ArrayList<String> receiveMessage() throws Exception{ //receives the message
        String response;
        String[] splitString;
        ArrayList<String> serverReply = new ArrayList<>();
        BufferedReader inFromServer = new BufferedReader( // input
                new InputStreamReader(commSocket.getInputStream()));
        do{ // since we do not know if 1 message or more, loop until flag says so
            response = inFromServer.readLine();
            splitString = response.split("/");
            serverReply.add(response);
        }while(!(splitString[1].equals("-")));
        return serverReply;
    }
    public void terminate(){
        try {
            this.commSocket.close();
        }catch(IOException e){
            System.out.println("Unable to close socket");
        }
    }
}
