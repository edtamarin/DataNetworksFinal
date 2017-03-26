import java.util.Scanner;

/**
 * Created by Egor Tamarin on 26-Mar-17.
 * The main class for the stock exchange client.
 */
public class SE_Client {
    static boolean clientToken;
    final static String address = "localhost";
    final static int port = 8000;
    public static void main(String args[]){
        System.out.println("Welcome to the Saxion Stock Exchange.");
        System.out.println("Please log in to proceed.");
        String login = pollUser("LOGIN:");
        String password = pollUser("PASSWORD:");
        Authorizator auth= new Authorizator(login,password);
        try {
            clientToken = auth.authorizeUser(address, port);
        }catch(Exception e){
            e.printStackTrace();
        }
        if (clientToken){

        }else{
            System.out.println("Authorization failed. Try again.");
        }
        System.out.print(login+" "+password);
    }
    private static String pollUser(String prompt){
        Scanner userInput = new Scanner(System.in);
        String userResponse;
        System.out.println(prompt);
        userResponse = userInput.nextLine();
        return userResponse;
    }
}
