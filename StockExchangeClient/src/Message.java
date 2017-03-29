import java.util.ArrayList;

/**
 * Created by Egor Tamarin on 26-Mar-17.
 * This class handles messages, both incoming and outgoing.
 */
public class Message {
    private String msgToValidate;
    private String msgToSend;
    private String[] possibleHeaders = {"SELL","BUY","INFO","STKI"};
    public Message(){
        this.msgToSend = "";
    }
    public Message(String message){
        this.msgToValidate = message;
        this.msgToSend = "";
    }
    public void authMessage(String lo, String pw){
        String msg = "AUTH/"+lo+":"+pw+"\r\n";
        this.msgToSend = msg;
    }
    public void constructMessage(){ // constructing the message
        String[] splitMessage = this.msgToValidate.split(" ");
        for (String header:possibleHeaders){ // check header validity
            if (splitMessage[0].toUpperCase().equals(header)){ // if valid
                this.msgToSend = splitMessage[0].toUpperCase(); // add to message
                switch(this.msgToSend){ // depending on header construct a message
                    case "SELL": // sell message
                        if (!(splitMessage.length < 3)){
                            this.msgToSend += "/"+splitMessage[1];
                            if (tryParseInt(splitMessage[2])){
                                this.msgToSend += ":"+splitMessage[2]+"\r\n";
                            }
                        }
                        break;
                    case "BUY": // buy message, payload identical to SELL
                        if (!(splitMessage.length < 3)){
                            this.msgToSend += "/"+splitMessage[1];
                            if (tryParseInt(splitMessage[2])){
                                this.msgToSend += ":"+splitMessage[2]+"\r\n";
                            }
                        }
                        break;
                    case "INFO":
                        this.msgToSend += "/0\r\n";
                        break;
                    case "STKI":
                        this.msgToSend += "/0\r\n";
                    default:
                        this.msgToSend = "invalid";
                        break;
                }
            }
        }
    }
    public int analyzeMessage(ArrayList<String> replyMessage){
        //here we try to analyze what the server replied with
        int responseCode = 0;
        String[] splitMessage;
        for (String message:replyMessage){
            splitMessage = message.split("/");
            switch (splitMessage[0]){
                case "101": // AUTH fail
                    System.out.println("Authorization failed. Try again.");
                    responseCode = 111;
                    break;
                case "102": // SELL fail
                    break;
                case "103": // BUY fail
                    break;
                case "104": //INFO fail
                    break;
                case "105": //STKI fail
                    break;
                case "201": // AUTH success
                    System.out.println("Authorized.");
                    break;
                case "202": // SELL success
                    break;
                case "203": // BUY success
                    break;
                case "204": //INFO success
                    break;
                case "205": //STKI success
                    break;
                default:
                    System.out.println("Reply not recognized");
                    break;
            }
        }
        return responseCode;
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
