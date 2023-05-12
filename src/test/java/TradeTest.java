import controller.TradeController;
import model.*;
import model.building.Building;
import model.building.BuildingType;
import model.building.Stock;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.function.Executable;

import java.util.HashMap;

public class TradeTest {
    TradeController tradeController = new TradeController();
    User userSendder = new User("ehsan", "mohtaram", "ehsanmohtaram", "ehsanmohtaram2004@gmail.com",
            "null", 1, "hi");
    User userReceiver = new User("ali", "mohtaram", "alimohtaram", "alimohtaram@gmail.com",
            "null", 1, "hi");
    Trade difaultTrade = new Trade(ResourceType.WOOD, 10, 10, userSendder, userReceiver, "salam", 1);
    Trade releventDifaultTrade = new Trade(ResourceType.WOOD, 10, 10, userReceiver, userSendder, "salam", 1);

    Kingdom kingdom = new Kingdom(Flags.RED, userSendder);
    Map map = new Map(10 , 10, "temporary");


    @BeforeAll
    public void beforeAll() {
        map.getPlayers().add(kingdom);
        tradeController.setCurrentUser(userSendder);
        tradeController.setGameMap(map);
    }


    @Test
    public void equalTrade() {
        Trade trade1 = new Trade(ResourceType.WOOD, 10, 10, null, null, "salam", 1);
        Trade trade2 = new Trade(ResourceType.WOOD, 10, 10, null, null, "salam", 1);
        Assertions.assertEquals(trade1, trade2);
    }


    public HashMap<String, String> getHashMap() {
        HashMap<String , String> options = new HashMap<>();
        options.put("u", "ehsan");
        options.put("t", "wood");
        options.put("a", "10");
        options.put("p", "10");
        options.put("m", "hello");
        return options;
    }

    @Test
    public void newRequestAccept() {
        beforeAll();
        Stock stock = new Stock(null, BuildingType.STOCKPILE, kingdom);
        kingdom.addBuilding(stock);
        String result = tradeController.newRequest(getHashMap());
        Assertions.assertEquals(result, "The request was successfully registered");
    }

    @Test
    public void newRequestNotEnoughSpace() {
        beforeAll();
        String result = tradeController.newRequest(getHashMap());
        Assertions.assertEquals(result, "You do not have enough space for this amount of resource!");
    }
////////
    @Test
    public void newRequestNotEnoughMoney() {
        beforeAll();
        HashMap<String, String> options = getHashMap();
        options.replace("p", "10000");
        String result = tradeController.newRequest(getHashMap());
        Assertions.assertEquals(result, "You do not have enough space for this amount of resource!");
    }

    @Test
    public void newRequestNotTrueFormatForNumber() {
        beforeAll();
        HashMap<String, String> options = getHashMap();
        options.replace("p", "a");
        String result = tradeController.newRequest(options);
        Assertions.assertEquals(result, "Please input digit as your values!");
    }
///////

    @Test
    public void newRequestUserNotFound() {
        HashMap<String, String > options =getHashMap();
        options.replace("u" , "u1");
        String result = tradeController.newRequest(options);
        Assertions.assertEquals(result, "Username with this ID was not found");
    }



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
        kingdom.getMySuggestion().add(difaultTrade);
        String result = tradeController.showTradeList();
        Assertions.assertEquals("your suggestions :\n" +
                "Resource type : WOOD\n" +
                "resource amount : 10\n" +
                "price : 10\n" +
                "from : ehsan\n" +
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
        kingdom.getHistoryTrade().add(difaultTrade);
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
        kingdom.getHistoryTrade().add(releventDifaultTrade);
        String result = tradeController.showTradeHistory();
        Assertions.assertEquals("your history:\n" +
                "Resource type : WOOD\n" +
                "resource amount : 10\n" +
                "price : 10\n" +
                "id: 1\n" +
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
        kingdom.getNotification().add(difaultTrade);
        difaultTrade.setMassageAccept("bye");
        String result = tradeController.showNotification();
        Assertions.assertEquals("your new notification :\n" +
                "your request accepted by--> ali\n" +
                "massage: bye", result);
    }

    @Test
    public void showNotificationAccepted() {
        beforeAll();
        kingdom.getNotification().add(releventDifaultTrade);
        String result = tradeController.showNotification();
        Assertions.assertEquals("your new notification :\n" +
                "new suggestion by--> ali\n" +
                "massage: salam", result);
    }

}
