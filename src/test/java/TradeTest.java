import controller.TradeController;
import model.*;
import model.building.BuildingType;
import model.building.Stock;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.function.Executable;
import java.util.HashMap;

public class TradeTest {
    TradeController tradeController = new TradeController();
    User userSendder = new User("ali", "mohtaram", "alimohtaram", "alimohtaram2004@gmail.com",
            "null", 1, "hi");
    User userReceiver = new User("ehsan", "mohtaram", "ehsanmohtaram", "ehsanmohtaram@gmail.com",
            "null", 1, "hi");
    Trade difaultTrade = new Trade(ResourceType.WOOD, 10, 10, userSendder, userReceiver, "salam", 1);
    Trade releventDifaultTrade = new Trade(ResourceType.WOOD, 10, 10, userReceiver, userSendder, "salam", 2);
    Kingdom sendderKingdom = new Kingdom(Flags.RED, userSendder);
    Kingdom receiverKingdom = new Kingdom(Flags.BLUE, userReceiver);
    Map map = new Map(10 , 10, "temporary");


    @BeforeAll
    public void beforeAll() {
        map.getPlayers().add(sendderKingdom);
        map.getPlayers().add(receiverKingdom);
        tradeController.setCurrentUser(userSendder);
        tradeController.setGameMap(map);
    }

    public HashMap<String, String> getHashMapForRequest() {
        HashMap<String , String> options = new HashMap<>();
        options.put("u", "ehsan");
        options.put("t", "wood");
        options.put("a", "10");
        options.put("p", "10");
        options.put("m", "hello");
        return options;
    }

    public HashMap<String, String> getHashMapForAccept() {
        HashMap<String, String> options = new HashMap<>();
        options.put("i" , "1");
        options.put("m", "salam");
        return options;
    }



    @Test
    public void newRequestAccept() {
        beforeAll();
        Stock stock = new Stock(null, BuildingType.STOCKPILE, sendderKingdom);
        sendderKingdom.addBuilding(stock);
        String result = tradeController.newRequest(getHashMapForRequest());
        Assertions.assertEquals(result, "The request was successfully registered");
    }

    @Test
    public void newRequestNotEnoughSpace() {
        beforeAll();
        String result = tradeController.newRequest(getHashMapForRequest());
        Assertions.assertEquals(result, "You do not have enough space for this amount of resource!");
    }
    @Test
    public void newRequestInvalidBound() {
        beforeAll();
        HashMap<String, String> options = getHashMapForRequest();
        options.replace("p", "-10");
        String result = tradeController.newRequest(options);
        Assertions.assertEquals(result, "Invalid bounds!");
    }
    //////
    @Test
    public void newRequestNotEnoughMoney() {
        beforeAll();
        HashMap<String, String> options = getHashMapForRequest();
        options.replace("p", "99999999");
        String result = tradeController.newRequest(options);
        Assertions.assertEquals(result, "You do not have enough space for this amount of resource!");
    }
    /////
    @Test
    public void newRequestNotTrueFormatForNumber() {
        beforeAll();
        HashMap<String, String> options = getHashMapForRequest();
        options.replace("p", "a");
        String result = tradeController.newRequest(options);
        Assertions.assertEquals(result, "Please input digit as your values!");
    }

    @Test
    public void newRequestUserNotFound() {
        HashMap<String, String > options = getHashMapForRequest();
        options.replace("u" , "u1");
        String result = tradeController.newRequest(options);
        Assertions.assertEquals(result, "Username with this ID was not found");
    }



    @Test
    public void acceptSuccesful() {
        beforeAll();
        sendderKingdom.addSuggestion(difaultTrade);
        String result = tradeController.tradeAccept(getHashMapForAccept());
        Assertions.assertEquals(result, "The trade was successful");
    }

    @Test
    public void acceptNotFoundID() {
        beforeAll();
        String result = tradeController.tradeAccept(getHashMapForAccept());
        Assertions.assertEquals(result, "This ID was not found for you");
    }


//    @Test void acceptNotEnoughResourceAmount() {
//        beforeAll();
////        HashMap<String, String> options = getHashMapForAccept();
////        options.replace("")
//        String result = tradeController.tradeAccept(getHashMapForAccept());
//        Assertions.assertEquals(result, "The resource balance is not enough");
//    }



    @Test
    public void showTradeListError() throws Exception {
        Assertions.assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                tradeController.showTradeList();
            }
        });
    }

    @Test
    public void showTradeList() {
        beforeAll();
        sendderKingdom.getMySuggestion().add(difaultTrade);
        String result = tradeController.showTradeList();
        Assertions.assertEquals("your suggestions :\n" +
                "Resource type : WOOD\n" +
                "resource amount : 10\n" +
                "price : 10\n" +
                "from : ali\n" +
                "id : 1\n" +
                "massage : salam", result);
    }


    @Test
    public void showTradeHistoryError() throws Exception {
        Assertions.assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                tradeController.showTradeHistory();
            }
        });
    }

    @Test
    public void showTradeHistoryRequested() {
        beforeAll();
        sendderKingdom.getHistoryTrade().add(difaultTrade);
        String result = tradeController.showTradeHistory();
        Assertions.assertEquals("your history:\n" +
                "Resource type : WOOD\n" +
                "resource amount : 10\n" +
                "price : 10\n" +
                "id: 1\n" +
                "massage : salam\n" +
                "...Requested...", result);
    }

    @Test
    public void showTradeHistoryAccepted() {
        beforeAll();
        sendderKingdom.getHistoryTrade().add(releventDifaultTrade);
        String result = tradeController.showTradeHistory();
        Assertions.assertEquals("your history:\n" +
                "Resource type : WOOD\n" +
                "resource amount : 10\n" +
                "price : 10\n" +
                "id: 2\n" +
                "massage : salam ...Accepted...", result);
    }


    @Test
    public void showNotificationError() throws Exception {
        Assertions.assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                tradeController.showNotification();
            }
        });
    }

    @Test
    public void showNotificationRequested() {
        beforeAll();
        sendderKingdom.getNotification().add(difaultTrade);
        difaultTrade.setMassageAccept("bye");
        String result = tradeController.showNotification();
        Assertions.assertEquals("your new notification :\n" +
                "your request accepted by--> ehsan\n" +
                "massage: bye", result);
    }

    @Test
    public void showNotificationAccepted() {
        beforeAll();
        sendderKingdom.getNotification().add(releventDifaultTrade);
        String result = tradeController.showNotification();
        Assertions.assertEquals("your new notification :\n" +
                "new suggestion by--> ehsan\n" +
                "massage: salam", result);
    }


    @Test
    public void equalTrade() {
        Trade trade1 = new Trade(ResourceType.WOOD, 10, 10, null, null, "salam", 1);
        Trade trade2 = new Trade(ResourceType.WOOD, 10, 10, null, null, "salam", 1);
        Assertions.assertEquals(trade1, trade2);
    }



}
