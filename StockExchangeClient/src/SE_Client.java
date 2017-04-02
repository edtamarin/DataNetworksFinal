import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Egor Tamarin on 26-Mar-17.
 * The main class for the stock exchange client.
 */
public class SE_Client {
    static boolean clientToken = true;
    final static String address = "localhost";
    final static int port = 8000;
    private static boolean running = true;
    public static void main(String args[]){
        // welcoming users
        System.out.println("Welcome to the Saxion Stock Exchange.");
        System.out.println("Please log in to proceed.");
        while (!clientToken) { //run login until correct
            String login = pollUser("LOGIN:");
            String password = pollUser("PASSWORD:");
            Authorizator auth = new Authorizator(login, password);
            try {
                clientToken = auth.authorizeUser(address, port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // now that we've logged in, we can do everything else
        System.out.println("Authorization successful.");
        System.out.println("Type 'help' for help.\n");
        String userIn;
        String systemResponse;
        Communication comms = new Communication();
        ArrayList<String> serverResponse = new ArrayList<>();
        while (running){
            userIn = pollUser(">>");
            switch (userIn.toUpperCase()) {
                case "HELP":
                    System.out.println("The program supports the following commands:");
                    System.out.println("buy <stockName> <quantity>");
                    System.out.println("sell <stockName> <quantity> <price>");
                    System.out.println("dpst <amount> - deposits money");
                    System.out.println("info - returns user info");
                    System.out.println("stki - returns stock info");
                    break;
                case "EXIT":
                    running =! running;
                    break;
                default:
                Message userRequest = new Message(userIn);
                userRequest.constructMessage();
                systemResponse = userRequest.getMsgToSend();
                switch (systemResponse) {
                    case "invalid":
                        System.out.println("Message is invalid and will not be sent. Try again.");
                        break;
                    default:
                        try {
                            comms.createSocket(address,port);
                            comms.sendMessage(userRequest);
                            serverResponse = comms.receiveMessage();
                            userRequest.analyzeMessage(serverResponse);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
                // analyze the server response
                // the response code is still being returned but should be used mainly for debugging
                break;
            }
        }
        comms.terminate();
        System.out.println("Closing the connection");
    }
    private static String pollUser(String prompt){
        Scanner userInput = new Scanner(System.in);
        String userResponse;
        System.out.print(prompt);
        userResponse = userInput.nextLine();
        return userResponse;
    }
}
