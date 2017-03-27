/**
 * Created by Egor Tamarin on 26-Mar-17.
 * This class handles messages, both incoming and outgoing.
 */
public class Message {
    private String msgToValidate;
    private String msgToSend;
    private String[] possibleHeaders = {"SELL","BUY","INFO"};
    public Message(){
    }
    public Message(String message){
        this.msgToValidate = message;
        this.msgToSend = "";
    }
    public String authMessage(String lo, String pw){
        String msg = "AUTH/"+lo+":"+pw+"\r\n";
        return msg;
    }
    public void constructMessage(){
        String[] splitMessage = this.msgToValidate.split(" ");
        for (String header:possibleHeaders){ // check header validity
            if (splitMessage[0].toUpperCase().equals(header)){ // if valid
                msgToSend = splitMessage[0].toUpperCase(); // add to message
                switch(msgToSend){ // depending on header construct a message
                    case "SELL": // sell message
                        if (!(splitMessage.length < 3)){
                            msgToSend += "/"+splitMessage[1];
                            if (tryParseInt(splitMessage[2])){
                                msgToSend += ":"+splitMessage[2]+"\r\n";
                            }
                        }
                        break;
                    case "BUY": // buy message, payload identical to SELL
                        if (!(splitMessage.length < 3)){
                            msgToSend += "/"+splitMessage[1];
                            if (tryParseInt(splitMessage[2])){
                                msgToSend += ":"+splitMessage[2]+"\r\n";
                            }
                        }
                        break;
                    case "INFO":
                        msgToSend += "/0\r\n";
                        break;
                    default:
                        msgToSend = "invalid";
                        break;
                }
            }
        }
    }
    public String getMsgToSend(){
        return this.msgToSend;
    }
    private boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
