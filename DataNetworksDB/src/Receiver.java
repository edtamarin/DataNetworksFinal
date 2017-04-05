import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * Created by Bence on 2017.04.04..
 */

public class Receiver {

    private String username;
    private String password;
    private String stockname;
    private int stockamount;
    private float stockprice;
    private float money;
    private String seller;

    ServerSocket welcomeSocket;
    Socket connectionSocket;
    String[] splitHeader;

    public void receiveTCP() throws Exception {
        String clientSentence;
        welcomeSocket = new ServerSocket(8874);
        String[] splitData;
        connectionSocket = welcomeSocket.accept();

        while (true) {
            BufferedReader inFromClient = new BufferedReader
                    (new InputStreamReader(connectionSocket.getInputStream()));
            clientSentence = inFromClient.readLine();
            System.out.println("Received: " + clientSentence);
            if (clientSentence.contains("/")) {
                splitHeader = clientSentence.split("/");
                switch (splitHeader[0]) {
                    case "AUTH":
                        splitData = splitHeader[1].split(":");
                        username = splitData[0];
                        password = splitData[1];
                        break;
                    case "BUY":
                        splitData = splitHeader[1].split(":");
                        stockname = splitData[0];
                        stockamount = Integer.valueOf(splitData[1]);
                        seller = splitData[2];
                        break;
                    case "SELL":
                        splitData = splitHeader[1].split(":");
                        stockname = splitData[0];
                        stockamount = Integer.valueOf(splitData[1]);
                        stockprice = Float.valueOf(splitData[2]);
                        break;
                    case "DEPT":
                        splitData = splitHeader[1].split(":");
                        money = Float.valueOf(splitData[1]);
                        break;
                    case "INFO":
                        break;
                    case "STKI":
                        stockname = splitHeader[1];
                        break;
                    default:
                        System.out.println("Not recognized");
                        break;
                }
                break;
            } else {
                throw new IllegalArgumentException("String " + clientSentence + " does not contain /");
            }
        }
    }

    public String getSplitHeader() {
        return splitHeader[0];
    }

    public void sendMessage(ArrayList<String> al) throws Exception {
        try {
            PrintWriter out = new PrintWriter(connectionSocket.getOutputStream(), true);
            for (String message : al) {
                out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getStockamount() {
        return stockamount;
    }

    public String getStockname() {
        return stockname;
    }

    public float getStockprice() {
        return stockprice;
    }

    public float getMoney() {
        return money;
    }

    public String getSeller() {
        return seller;
    }
}