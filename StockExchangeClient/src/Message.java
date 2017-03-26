/**
 * Created by Egor Tamarin on 26-Mar-17.
 * This class handles the message creation.
 */
public class Message {
    public Message(){

    }
    public String authMessage(String lo, String pw){
        String msg = "AUTH/"+lo+":"+pw+"\r\n";
        return msg;
    }
    public String sellMessage(String name, int amount){

    }
    public String buyMessage(String name, int amount){

    }
    public String infoMessage(){

    }
}
