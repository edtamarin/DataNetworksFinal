import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * Created by Bence on 2017.04.03..
 */
public class StockDAO {

    String message;
    char flag = '+';
    ArrayList<String> messagesOut = new ArrayList<>();

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    public static Connection getConnection() throws SQLException, IOException {
        Properties props = new Properties();
        FileInputStream in = new FileInputStream("database.properties");
        props.load(in);
        in.close();

        String drivers = props.getProperty("jdbc.drivers");
        if (drivers != null) System.setProperty("jdbc.drivers", drivers);
        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        return DriverManager.getConnection(url, username, password);
    }

    public void getAuthentication(String username, String password) throws Exception {
        Connection conn = getConnection();
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT USERID, PASSWORD " +
                            "FROM USERS " +
                            "WHERE USERID LIKE ? AND PASSWORD LIKE ?");
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                message = "201/-/0";
            } else {
                message = "101/-/0";
            }
        } catch (SQLException se) {
            message = "101/-/0";
        }
        messagesOut.add(message);
    }

    public void sellStock(String stockname, String seller, int amount, float price) throws Exception {
        Connection conn = getConnection();
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE " + stockname +
                            " SET FORSALE = FORSALE + " + amount +
                            " WHERE STOCKOWNER LIKE ?");

            pstmt.setString(1, seller);
            pstmt.executeUpdate();
            float profit = amount * price;
            message = "202/-/" + profit;
        } catch (SQLException e) {
            message = "102/-/0";
        }
        messagesOut.add(message);
    }

    public void buyStock(String stockname, int amount, String username, String seller) throws Exception {
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE " + stockname +
                            " SET AMOUNTOFSTOCK = AMOUNTOFSTOCK + " + amount +
                            " WHERE STOCKOWNER LIKE ?");
            pstmt.setString(1, username);
            pstmt.executeUpdate();

            PreparedStatement pstmt2 = conn.prepareStatement(
                    "UPDATE " + stockname +
                            " SET AMOUNTOFSTOCK = AMOUNTOFSTOCK - " + amount +
                            " AND " +
                            " WHERE STOCKOWNER LIKE ?");
            pstmt2.setString(1, seller);
            pstmt2.executeUpdate();

            PreparedStatement pstmt3 = conn.prepareStatement(
                    "UPDATE USERS " +
                            " SET BALANCE = BALANCE - " + (amount * 5) +
                            " WHERE USERID LIKE ?");
            pstmt3.setString(1, username);
            pstmt3.executeUpdate();

            PreparedStatement pstmt4 = conn.prepareStatement(
                    "UPDATE USERS " +
                            " SET BALANCE = BALANCE + " + (amount * 5) +
                            " WHERE USERID LIKE ?");
            pstmt4.setString(1, seller);
            pstmt4.executeUpdate();

            PreparedStatement pstmt5 = conn.prepareStatement(
                    "UPDATE " + stockname +
                            " SET FORSALE = FORSALE - " + amount +
                            " WHERE STOCKOWNER LIKE ?");
            pstmt5.setString(1, seller);
            pstmt5.executeUpdate();
            message = "203/-/0";

        } catch (SQLException se) {
            message = "103/-/0";
        }
        messagesOut.add(message);

    }

    public void cashIn(String username, float value) throws Exception {
        Connection conn = getConnection();
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE USERS " +
                            "SET BALANCE = BALANCE + ? " +
                            "WHERE USERID LIKE ?");
            pstmt.setFloat(1, value);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
            message = "206/-/0";
        } catch (SQLException se) {
            message = "106/-/0";
        }
        messagesOut.add(message);
    }

    public void getStockInfo(String stockname) throws Exception {
        Connection conn = getConnection();
        try {

            PreparedStatement pstmt = conn.prepareStatement(
                    " SELECT * FROM " + stockname);

            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                if (rs.isLast()) {
                    flag = '-';
                }
                message = "205/" + flag + "/";
                for (int i = 1; i < columnsNumber + 1; i++)
                    message = message + rs.getString(i) + ":";
                messagesOut.add(removeLastChar(message));
            }
        } catch (SQLException se) {
            message = "105/-/0";
            messagesOut.add(message);
        }
    }

    public void getUserInfo(String username) throws Exception {
        Connection conn = getConnection();
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT STOCKOWNER, AMOUNTOFSTOCK, ABBREVIATION, BALANCE " +
                            "FROM WATTSOCIETY, USERS " +
                            "WHERE STOCKOWNER LIKE ? AND USERS.USERID LIKE ? " +
                            "UNION " +
                            "SELECT STOCKOWNER, AMOUNTOFSTOCK, ABBREVIATION, BALANCE " +
                            "FROM ARCHIMEDESSOCIETY, USERS " +
                            "WHERE STOCKOWNER LIKE ? AND USERS.USERID LIKE ? ");
            pstmt.setString(1, username);
            pstmt.setString(2, username);
            pstmt.setString(3, username);
            pstmt.setString(4, username);

            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                if (rs.isLast()) {
                    flag = '-';
                }
                String message = "204/" + flag + "/";
                for (int i = 1; i < columnsNumber + 1; i++)
                    message = message + rs.getString(i) + ":";
                messagesOut.add(removeLastChar(message));
            }
        } catch (SQLException se) {
            message = "104/-/0";
            messagesOut.add(message);
        }
    }

    public ArrayList<String> getMessagesOut() {
        return messagesOut;
    }
}

