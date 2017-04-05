import java.util.ArrayList;

/**
 * Created by Egor Tamarin on 26-Mar-17.
 * This class handles messages, both incoming and outgoing.
 */
public class Message {
    private String msgToValidate;
    private String msgToSend;
    private String[] possibleHeaders = {"SELL","BUY","INFO","STKI","DPST"};
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
    public void constructMessage(String uName){ // constructing the message
        String[] splitMessage = this.msgToValidate.split(" ");
        boolean isValid = false;
        int validIndex = 0;
        for (int i = 0; i<possibleHeaders.length; i++){
            if (splitMessage[0].toUpperCase().equals(possibleHeaders[i])){
                isValid = true;
                validIndex = i;
            }
        }
            if (isValid){ // if valid
                this.msgToSend = splitMessage[0].toUpperCase(); // add to message
                switch(this.msgToSend){ // depending on header construct a message
                    case "SELL": // sell message
                        if ((splitMessage.length ==4)){
                            this.msgToSend += "/"+splitMessage[1];
                            if (tryParseInt(splitMessage[2])){
                                this.msgToSend+=":"+splitMessage[2];
                            }
                            if (isNumeric(splitMessage[3])){
                                this.msgToSend+=":"+splitMessage[3]+"\r\n";
                            }
                        }
                        break;
                    case "BUY": // buy message, payload identical to SELL
                        if ((splitMessage.length ==4)){
                            this.msgToSend += "/"+splitMessage[1];
                            if (tryParseInt(splitMessage[2])){
                                this.msgToSend+=":"+splitMessage[2];
                            }
                            this.msgToSend+=":"+splitMessage[3]+"\r\n";
                        }
                        break;
                    case "INFO":
                            this.msgToSend += "/"+uName+"\r\n";
                        break;
                    case "STKI":
                        if ((splitMessage.length ==2)){
                                this.msgToSend += "/"+splitMessage[1]+"\r\n";
                        }
                        break;
                    case "DEPT":
                        if ((splitMessage.length ==2)){
                            if (tryParseInt(splitMessage[1])){
                                this.msgToSend += "/"+splitMessage[1]+"\r\n";
                            }
                        }
                        break;
                    default:
                        break;
                }
            }else{
                this.msgToSend = "invalid";
            }
    }
    public int analyzeMessage(ArrayList<String> replyMessage){
        //here we try to analyze what the server replied with
        int responseCode = 0;
        String[] splitMessage;
        String[] splitData;
        for (String message:replyMessage){ // for each server reply
            splitMessage = message.split("/"); // split the response for processing
            switch (splitMessage[0]){
                case "101": // AUTH fail
                    if ((splitMessage.length == 3)){
                        if (splitMessage[2].equals("ERROR")){ // check error codes
                            System.out.println("Authorization failed.");
                        }
                    }else{
                        System.out.println("AUTH fail reply not recognized");
                    }
                    responseCode = 111;
                    break;
                case "102": // SELL fail
                    if ((splitMessage.length == 3)){
                            System.out.println("You might not have enough stocks or there are no such stocks. INFO for info.");
                    }else{
                        System.out.println("SELL fail reply not recognized");
                    }
                    responseCode = 112;
                    break;
                case "103": // BUY fail
                    if ((splitMessage.length == 3)){
                            System.out.println("You might not have enough money or there are no such stocks. INFO for info.");
                    }else{
                        System.out.println("BUY fail reply not recognized");
                    }
                    responseCode = 113;
                    break;
                case "104": //INFO fail
                    if ((splitMessage.length == 3)){
                            System.out.println("Something went wrong.");
                    }else{
                        System.out.println("INFO fail reply not recognized");
                    }
                    responseCode = 114;
                    break;
                case "105": //STKI fail
                    if ((splitMessage.length == 3)){
                            System.out.println("There is probably no such stock.");
                    }else{
                        System.out.println("STKI fail reply not recognized");
                    }
                    responseCode = 115;
                    break;
                case "106": //DPST fail
                    if ((splitMessage.length == 3)){
                            System.out.println("Operation failed. Try again later.");
                    }else{
                        System.out.println("DPST fail reply not recognized");
                    }
                    responseCode = 116;
                    break;
                case "201": // AUTH success
                    System.out.println("Authorized.");
                    responseCode = 211;
                    break;
                case "202": // SELL success
                    if ((splitMessage.length == 3)){
                        System.out.println("Request approved.");
                        System.out.println("Expected profit: "+ splitMessage[2]);
                    }else{
                        System.out.println("SELL success reply not recognized");
                    }
                    break;
                case "203": // BUY success
                    if ((splitMessage.length == 3)){
                        System.out.println("Request approved.");
                    }else{
                        System.out.println("SELL success reply not recognized");
                    }
                    break;
                case "204": //INFO success
                    if ((splitMessage.length == 3)){
                        splitData = splitMessage[2].split(":");
                        System.out.println("You have "+splitData[1]+" stocks of "+splitData[2]);
                        System.out.println("Your balance is " + splitData[3] + " EUR.");
                    }else{
                        System.out.println("INFO success reply not recognized");
                    }
                    break;
                case "205": //STKI success
                    if ((splitMessage.length == 3)){
                        splitData = splitMessage[2].split(":");
                        System.out.println(splitData[0] + " has " + splitData[1] + " ; worth" +
                                " " + splitData[2] + " EUR. " + splitData[4] + " are for sale.");
                    }else{
                        System.out.println("INFO success reply not recognized");
                    }
                    break;
                case "206": //DEPT success
                    if ((splitMessage.length == 3)){
                        System.out.println("Deposit successful.");
                    }else{
                        System.out.println("SELL success reply not recognized");
                    }
                    break;
                default:
                    System.out.println("Reply not recognized");
                    break;
            }
        }
        return responseCode;
    }
    public boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
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
