import controller.TradeController;
import model.*;
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

//    @Test
//    public void newRequestAccept() {
//        HashMap<String, String > options = new HashMap<>();
//        User user = new User("ehsan", "mohtaram", "ehsanmohtaram", "ehsanmohtaram2004@gmail.com",
//                "null", 1, "hi");
//        options.put("u", "ehsan");
//        options.put("t", "wood");
//        options.put("a", "10");
//        options.put("p", "10");
//        options.put("m", "hello");
//        TradeController tradeController = new TradeController();
//        String result = tradeController.newRequest(options);
//        Assert.assertEquals(result, "The request was successfully registered");
//    }


    @Test
    public void newRequestUserNotFound() {
        HashMap<String, String > options = new HashMap<>();
        options.put("u", "u1");
        options.put("t", "wood");
        options.put("a", "10");
        options.put("p", "10");
        options.put("m", "hello");
        TradeController tradeController = new TradeController();
        String result = tradeController.newRequest(options);
        Assertions.assertEquals(result, "Username with this ID was not found");
    }

//    @Test
//    public void newRequestBalanceAmount() {
//        HashMap<String, String > options = new HashMap<>();
//        options.put("u", "ehsan");
//        options.put("t", "wood");
//        options.put("a", "10");
//        options.put("p", "100");
//        options.put("m", "hello");
//        TradeController tradeController = new TradeController();
//        String result = tradeController.newRequest(options);
//        Assert.assertEquals(result, "your balance not enough");
//    }




    @Test
    public void showTradeListError() throws Exception {
        Assertions.assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                TradeController tradeController = new TradeController();
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
                TradeController tradeController = new TradeController();
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

}
