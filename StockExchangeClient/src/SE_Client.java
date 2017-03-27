import java.util.Scanner;

/**
 * Created by Egor Tamarin on 26-Mar-17.
 * The main class for the stock exchange client.
 */
public class SE_Client {
    static boolean clientToken = false;
    final static String address = "localhost";
    final static int port = 8000;
    public static void main(String args[]){
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
            if (!clientToken) {
                System.out.println("Authorization failed. Try again.");
            }
        }
        // now that we've logged in, we can do everything else
        System.out.println("Authorization successful.");
        System.out.println("Type 'help' for help.\n");
        String userIn;
        String systemResponse;
        String serverResponse;
        while (true){
            userIn = pollUser(">>");
            Message userRequest = new Message(userIn);
            userRequest.constructMessage();
            systemResponse = userRequest.getMsgToSend();
            switch(systemResponse){
                case "invalid":
                    System.out.println("Message is invalid and will not be sent. Try again.");
                    break;
                default:
                    Communication comms = new Communication(address,port);
                    try {
                        serverResponse = comms.sendMessage(userRequest);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
    private static String pollUser(String prompt){
        Scanner userInput = new Scanner(System.in);
        String userResponse;
        System.out.print(prompt);
        userResponse = userInput.nextLine();
        return userResponse;
    }
}
