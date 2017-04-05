/**
 * Created by Bence on 2017.04.04..
 */
public class TestStock {

    public static void main(String args[]) throws Exception {
        StockDAO stockInstance = new StockDAO();
        Receiver receivedInfo = new Receiver();

        while (true) {
            receivedInfo.receiveTCP();
            stockInstance.getAuthentication(receivedInfo.getUsername(), receivedInfo.getPassword());

            switch (receivedInfo.getSplitHeader()) {
                case "BUY":
                    stockInstance.buyStock(receivedInfo.getStockname(), receivedInfo.getStockamount(), receivedInfo.getUsername(), receivedInfo.getSeller());
                    break;
                case "SELL":
                    stockInstance.sellStock(receivedInfo.getStockname(), receivedInfo.getUsername(), receivedInfo.getStockamount(), receivedInfo.getStockprice());
                    break;
                case "DEPT":
                    stockInstance.cashIn(receivedInfo.getUsername(), receivedInfo.getMoney());
                    break;
                case "INFO":
                    stockInstance.getUserInfo(receivedInfo.getUsername());
                    break;
                case "STKI":
                    stockInstance.getStockInfo(receivedInfo.getStockname());
                    break;
                default:
                    System.out.println("Not recognized");
                    break;
            }
            receivedInfo.sendMessage(stockInstance.getMessagesOut());
        }

    }
}
