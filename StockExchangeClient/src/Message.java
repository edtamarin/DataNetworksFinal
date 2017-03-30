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
    public void constructMessage(){ // constructing the message
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
                        if ((splitMessage.length ==3)){
                            this.msgToSend += "/"+splitMessage[1];
                            if (tryParseInt(splitMessage[2])){
                                this.msgToSend+=":"+splitMessage[2]+"\r\n";
                            }
                        }
                        break;
                    case "BUY": // buy message, payload identical to SELL
                        if ((splitMessage.length ==3)){
                            this.msgToSend += "/"+splitMessage[1];
                            if (tryParseInt(splitMessage[2])){
                                this.msgToSend+=":"+splitMessage[2]+"\r\n";
                            }
                        }
                        break;
                    case "INFO":
                        this.msgToSend += "/0\r\n";
                        break;
                    case "STKI":
                        this.msgToSend += "/0\r\n";
                        break;
                    case "DPST":
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
        for (String message:replyMessage){
            splitMessage = message.split("/");
            switch (splitMessage[0]){
                case "101": // AUTH fail
                    System.out.println("Authorization failed. Try again.");
                    responseCode = 111;
                    break;
                case "102": // SELL fail
                    if ((splitMessage.length == 3)){
                        if (splitMessage[2].equals("SNF")){ // check error codes
                            System.out.println("Stocks not found.");
                        }else if (splitMessage[2].equals("RTL")){
                            System.out.println("You don;t have enough stocks.");
                        }
                    }else{
                        System.out.println("SELL fail reply not recognized");
                    }
                    responseCode = 112;
                    break;
                case "103": // BUY fail
                    if ((splitMessage.length == 3)){
                        if (splitMessage[2].equals("SNF")){ // check error codes
                            System.out.println("Stocks not found.");
                        }else if (splitMessage[2].equals("RTL")){
                            System.out.println("There's not enough stocks on the market.");
                        }else if (splitMessage[2].equals("NEF")){
                            System.out.println("You don't have enough funds.");
                        }
                    }else{
                        System.out.println("BUY fail reply not recognized");
                    }
                    responseCode = 113;
                    break;
                case "104": //INFO fail
                    System.out.println("Unable to retrieve information.");
                    responseCode = 114;
                    break;
                case "105": //STKI fail
                    System.out.println("Can't retrieve market information.");
                    responseCode = 115;
                    break;
                case "106": //DEPT fail
                    System.out.println("Can't deposit money. Try again later.");
                    responseCode = 116;
                    break;
                case "201": // AUTH success
                    System.out.println("Authorized.");
                    responseCode = 211;
                    break;
                case "202": // SELL success
                    if ((splitMessage.length == 3)){
                        splitData = splitMessage[2].split(":");
                        System.out.println(splitData[1]+" stocks of "+splitData[0]+ " were placed on the market.");
                        System.out.println("Expected profit: "+splitData[2]);
                    }else{
                        System.out.println("SELL success reply not recognized");
                    }
                    break;
                case "203": // BUY success
                    if ((splitMessage.length == 3)){
                        splitData = splitMessage[2].split(":");
                        System.out.println(splitData[1]+" stocks of "+splitData[0]+ " were bought.");
                        System.out.println("Balance: "+splitData[2]);
                    }else{
                        System.out.println("SELL success reply not recognized");
                    }
                    break;
                case "204": //INFO success
                    if ((splitMessage.length == 3)){
                        splitData = splitMessage[2].split(":");
                        System.out.println("You have "+splitData[1]+" stocks of "+splitData[0]);
                    }else{
                        System.out.println("INFO success reply not recognized");
                    }
                    break;
                case "205": //STKI success
                    break;
                case "206": //DEPT success
                    if ((splitMessage.length == 3)){
                        System.out.println("Your account balance is now " + splitMessage[3] + " EUR.");
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
    public void clear(){
        this.msgToSend = "";
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
