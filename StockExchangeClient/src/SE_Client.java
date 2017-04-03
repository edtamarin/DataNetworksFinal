import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Egor Tamarin on 26-Mar-17.
 * The main class for the stock exchange client.
 */
public class SE_Client {
    static boolean clientToken = false;
    final static String address = "localhost";
    final static int port = 8000;
    private static boolean running = true;
    public static void main(String args[]){
        // welcoming users
        String username = "";
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
            username = auth.getLogin();
        }
        // now that we've logged in, we can do everything else
        System.out.println("Welcome, " + username);
        System.out.println("Type 'help' for help.\n");
        String userIn;
        String systemResponse;
        Communication comms = new Communication();
        ArrayList<String> serverResponse = new ArrayList<>();
        while (running){ // runs forever, polls user
            userIn = pollUser(">>");
            switch (userIn.toUpperCase()) { // user input selection
                case "HELP": // help prompt
                    System.out.println("The program supports the following commands:");
                    System.out.println("buy <stockName> <quantity>");
                    System.out.println("sell <stockName> <quantity> <price>");
                    System.out.println("dpst <amount> - deposits money");
                    System.out.println("info <username> - returns user info");
                    System.out.println("stki <stock>- returns stock info");
                    break;
                case "EXIT": // quits loop
                    running =! running;
                    break;
                default:
                Message userRequest = new Message(userIn); // create a message
                userRequest.constructMessage(username); // construct a message based on user input
                systemResponse = userRequest.getMsgToSend(); // see if it was correct or not
                switch (systemResponse) {
                    case "invalid": // command not recognized
                        System.out.println("Message is invalid and will not be sent. Try again.");
                        break;
                    default: // command recognized
                        try {
                            comms.createSocket(address,port); // open TCP connection
                            comms.sendMessage(userRequest); // send message
                            serverResponse = comms.receiveMessage(); // receive message
                            userRequest.analyzeMessage(serverResponse); // analyze it
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
        comms.terminate(); // close TCP connection
        System.out.println("Closing the connection");
    }
    private static String pollUser(String prompt){ // polls the user
        Scanner userInput = new Scanner(System.in);
        String userResponse;
        System.out.print(prompt);
        userResponse = userInput.nextLine();
        return userResponse;
    }
}
