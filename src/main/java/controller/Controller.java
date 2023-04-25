package controller;

import model.Database;
import model.Map;
import model.User;
import model.*;
import org.apache.commons.codec.digest.DigestUtils;
import view.*;

import java.util.*;
import java.util.regex.Matcher;

public class Controller {
    private LoginMenu loginMenu;
    private ProfileMenu profileMenu;

    private MainMenu mainMenu;

    private DesignMapMenu designMapMenu;
    private SignupMenu signupMenu;

    public static User currentUser = null;
    public static boolean stayLoggedIn = false;
    private Map gameMap;

    public Controller() {
        this.loginMenu = new LoginMenu(this);
        this.profileMenu = new ProfileMenu(this);
        this.mainMenu = new MainMenu(this);
        User.users.addAll(Database.setArrayOfUsers());
        for (User user : User.users) if (user.getLoggedIn()) currentUser = user;
    }

    public void run() {
        if (currentUser == null) if (loginMenu.run().equals("exit")) return;
        while (true) {
            switch (mainMenu.run()) {
                case "selectMap":
//                  currentUser.addToMyMap();
                    MapDesignController mapDesignController = new MapDesignController(gameMap);
                    mapDesignController.run();
                    break;
                case "profile":
                    profileMenu.run();
                    break;
                case "logout":
                    if (loginMenu.run().equals("exit"))
                        return;
                    break;
            }
        }
    }

    public String passwordValidation(String password, String passwordConfirmation) {
        if (!password.equals("random") && passwordConfirmation == null) {System.out.println("Please fill password confirmation!"); return null;}
        if (!password.equals("random") &&
                (password.length() < 6 || !password.matches(".*[a-z]+.*") ||
                        !password.matches(".*[A-Z]+.*") || !password.matches(".*[0-9]+.*") ||
                        !password.matches(".*\\W+.*"))) {System.out.println("Weak password. Please set a strong password!"); return null;}
        if (!password.equals("random") && !password.equals(passwordConfirmation))
        {System.out.println("Password confirmation did not match the password!"); return null;}
        if (password.equals("random")) {
            password = Randomize.randomPassword();
            if (password == null) {System.out.println("Please try again with new password!"); return null;}
        }
        return password;
    }

    public String createUser(HashMap<String, String> options) {
        String username = null; String password; String slogan;
        for (String key : options.keySet())
            if (!key.equals("s") && !key.equals("P") && options.get(key) == null) {System.out.println(key); return "Please input necessary options!";}
        for (String key : options.keySet())
            if (options.get(key) != null && options.get(key).equals("")) return "Illegal value. Please fill the options!";
        if (options.get("u").matches(".*[^A-Za-z0-9_]+.*")) return "Incorrect format of username!";
        if (User.getUserByUsername(options.get("u")) != null) {
            System.out.println("Username already exists!");
            username = Randomize.randomUsername(options.get("u"));
            if (username == null) return "Please try again with new username!";
        }
        if ((password = passwordValidation(options.get("p"), options.get("P"))) == null) return null;
        if (User.checkForEmailDuplication(options.get("e").toLowerCase())) return "Email already exists!";
        if (!options.get("e").matches("^[A-Za-z0-9_]+@[A-Za-z0-9_]+\\.[A-Za-z0-9_]+$")) return "Incorrect format of email!";
        if (username == null) username = options.get("u");
        if (options.get("s") != null && options.get("s").equals("random")) slogan = Randomize.randomSlogan();
        else slogan = options.get("s");
        password = DigestUtils.sha256Hex(password);
        return securityQuestion(username,password,options.get("n"),options.get("e"),slogan);
    }
    private String securityQuestion(String username, String password, String nikName, String email, String slogan) {
        System.out.println("Pick your security question: ");
        for (int i = 0; i < 3 ;i++) System.out.println((i + 1) + ". " + User.questions.get(i));
        CommandParser commandParser = new CommandParser();
        String inputCommand = CommandParser.getScanner().nextLine();
        HashMap<String, String> optionPass;
        optionPass = commandParser.validate(inputCommand,"question pick","q|question/a|answer/c|confirmation");
        if (optionPass == null) return "Invalid command!";
        for (String key : optionPass.keySet())
            if (optionPass.get(key) == null) return "Please input necessary options!";
        for (String key : optionPass.keySet())
            if (optionPass.get(key).equals("")) return "Illegal value. Please fill the options!";
        if (!optionPass.get("q").matches("-?\\d+")) return "Illegal value. Please choose a digit.";
        if (Integer.parseInt(optionPass.get("q")) < 1 ||
                Integer.parseInt(optionPass.get("q")) > 3) return "Out of bound. Please choose a digit between 1 to 3.";
        if (!optionPass.get("c").equals(optionPass.get("a"))) return "Answer did not match with confirmation";
        if (!Randomize.randomCaptcha().equals("done")) return "Captcha did not match with your input!";
        User.addUser(username,nikName,password,email,slogan,Integer.parseInt(optionPass.get("q")) - 1,optionPass.get("a"));
        return "User has been added successfully!";
    }
    public String login(HashMap<String, String> options) {
        for (String key : options.keySet())
            if (!key.equals("s") && options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet())
            if (options.get(key) != null && !key.equals("s") && options.get(key).equals("")) return "Illegal value. Please fill the options!";
        if (User.getUserByUsername(options.get("u")) == null) return "Username and password did not match!";
        if (User.getStatueOfDelayOfUser(options.get("u")).equals(true))
            return "You can not login for now. Please try in " +
                    (User.getTimeOfDelayOfUser(options.get("u")) - setDelay(options.get("u"))) + " seconds!";
        if (!User.getUserByUsername(options.get("u")).checkPassword(options.get("p"))) {
            setDelay(options.get("u"));
            return "Username and password did not match!";
        }
        if (!Randomize.randomCaptcha().equals("done")) return "Captcha did not match with your input!";
        if (options.get("s") != null) User.getUserByUsername(options.get("u")).setLoggedIn(true);
        currentUser = User.getUserByUsername(options.get("u"));
        return "login";
    }

    public Long setDelay(String username) {
        if (User.getStatueOfDelayOfUser(username).equals(false)) {
            User.setDelayForUser(username);
            if (User.getTimeOfDelayOfUser(username) > 0) {
                User.setStartOfDelay(username);
                User.setDelayStatue(username);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        User.setDelayStatue(username); timer.cancel();
                    }
                }, User.getTimeOfDelayOfUser(username) * 1000L);
            }
        }
        if (User.whenDelayStarted.containsKey(User.getUserByUsername(username)))
            return (System.currentTimeMillis() - User.getStartOfDelay(username))/1000 ;
        else return null;
    }

    public String forgetPassword(HashMap<String, String> options) {
        if (options.get("u") == null) return "Please input necessary options!";
        if (options.get("u").equals("")) return "Illegal value. Please fill the options!";
        System.out.println("Please answer the following question : ");
        System.out.print(User.questions.get(User.getUserByUsername(options.get("u")).getSecurityQuestionNumber()) + " ");
        String confirm;
        confirm = CommandParser.getScanner().nextLine();
        if (!confirm.equals(User.getUserByUsername(options.get("u")).getAnswerToSecurityQuestion())) return "Your answer did not match!";
        String inputCommand = CommandParser.getScanner().nextLine();
        CommandParser commandParser = new CommandParser();
        System.out.println("Set your new password like this : new password -p <password> <password confirmation>");
        HashMap<String, String> optionPass = commandParser.validate(inputCommand, "new password","?p|password");
        String password;
        if ((password = passwordValidation(optionPass.get("p"),optionPass.get("P"))) == null) return "Password change failed!";
        User.getUserByUsername(options.get("u")).setPassword(DigestUtils.sha256Hex(password));
        return "Password changed successfully";
    }

    public String profileChange(HashMap<String, String> options) {
        String username = null;
        int counter = 0;
        for (String key : options.keySet()) if (options.get(key) != null) counter++;
        if (counter > 1 || counter == 0) return "Please input only one option!";
        for (String key : options.keySet()) if (options.get(key) != null && options.get(key).equals("")) return "Illegal value. Please fill the options!";
        if (options.get("u") != null && options.get("u").matches(".*[^A-Za-z0-9_]+.*")) return "Incorrect format of username!";
        if (options.get("u") != null && User.getUserByUsername(options.get("u")) != null) {
            System.out.println("Username already exists!");
            username = Randomize.randomUsername(options.get("u"));
            if (username == null) return "Please try again with new username!";
        }
        if (options.get("u") != null && username == null) username = options.get("u");
        if (options.get("e") != null && User.checkForEmailDuplication(options.get("e").toLowerCase())) return "Email already exists!";
        if (options.get("e") != null && !options.get("e").matches("^[A-Za-z0-9_]+@[A-Za-z0-9_]+\\.[A-Za-z0-9_]+$")) return "Incorrect format of email!";
        if (options.get("u") != null) {currentUser.setUserName(username); return "Username changed successfully!";}
        if (options.get("e") != null) {currentUser.setEmail(options.get("e")); return "Email changed successfully!";}
        if (options.get("n") != null) {currentUser.setNickName(options.get("n")); return "Nickname changed successfully!";}
        if (options.get("s") != null) {currentUser.setSlogan(options.get("s")); return "Slogan changed successfully!";}
        return "Profile menu updated";
    }

    public String changePassword(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        if (!currentUser.checkPassword(options.get("o"))) return "Incorrect password! Please input your current password again!";
        String password = options.get("n");
        if (password.equals(options.get("o"))) return "Please enter a new password!";
        if (password.length() < 6 || !password.matches(".*[a-z]+.*") ||
                        !password.matches(".*[A-Z]+.*") || !password.matches(".*[0-9]+.*") ||
                        !password.matches(".*\\W+.*")) return "Weak password. Please set a strong password!";
        System.out.print("Please enter your new password again : ");
        String confirm = CommandParser.getScanner().nextLine();
        if (!confirm.equals(password)) return "Password confirmation did not match the password!";
        if (!Randomize.randomCaptcha().equals("done")) return "Captcha did not match with your input!";
        currentUser.setPassword(DigestUtils.sha256Hex(password));
        return "Password changed successfully!";
    }

    public String removeSlogan() {
        if (currentUser.getSlogan().equals("")) return "Your slogan filed is already empty!";
        else {currentUser.setSlogan(""); return "Slogan removed successfully!";}
    }

    public List<User> calculateRank() {
        List<User> sortUser = new ArrayList<>(User.users);
        sortUser.sort((u1, u2) -> {
            if (u1.getScore().compareTo(u2.getScore()) == 0)
                return u1.getUserName().compareTo(u2.getUserName());
            return u1.getScore().compareTo(u2.getScore());
        });
        return sortUser;
    }

    public String displayInfoSeparately(Boolean highScore, Boolean rank, Boolean slogan) {
        if (highScore) return "Your highest score in game is : " + currentUser.getScore();
        if (slogan) {
            if (currentUser.getSlogan().equals("")) return "Slogan is empty!";
            else return "Your slogan is : " + currentUser.getSlogan();
        }
        if (rank) return "Your rank is : " + (calculateRank().indexOf(currentUser) + 1);
        return "Info Displayed";

    }

    public String displayAllInfo() {
        return "Username : " + currentUser.getUserName() +
                "\nNickName : " + currentUser.getNickName() +
                "\nEmail : " + currentUser.getEmail() +
                "\nSlogan : " + currentUser.getSlogan() +
                "\nHighest Score : " + currentUser.getScore() +
                "\nRank : " + (calculateRank().indexOf(currentUser) + 1);
    }

    public String showDefaultMaps(){
        String result = "";
        for (Map defaults: Map.DEFAULT_MAPS) {
            result += defaults.getMapName() + ": " + defaults.getMapWidth() + " * " + defaults.getMapHeight() + '\n';
        }
        return result;
    }

    public String selectDefaultMap(String selectedIndex){
        try{
            int index = Integer.parseInt(selectedIndex);
            gameMap = Map.getDefaultMap(index);
            if(gameMap == null)
                return "invalid number";
            return "successful";
        }
        catch (Exception IllegalArgumentException){
            return "invalid input, please select a number";
        }
    }

    public String createNewMap(HashMap<String, String> options){

        if (options.get("x").equals("") || options.get("x") == null ||
            options.get("y").equals("") || options.get("y") == null) return "Please input width & height correctly ";
        if(options.get("n") == null || options.get("n").equals(""))
            return "you must choose a name for your map. use -n.";
        int width = Integer.parseInt(options.get("x"));
        int height = Integer.parseInt("y");
        if(width < 0 || height < 0)
            return "invalid bounds";
        gameMap = new Map(width, height, options.get("n"));
        return "successful";
    }

    public String showMap() {
        return null;
    }
    public String moveMap() {
        return null;
    }
    public String showMapDetails(Matcher matcher) {
        return null;
    }
    public String showPopularityFactors() {
        return null;
    }
    public String showPopularity() {
        return null;
    }
    public String showFoodList() {
        return null;
    }
    public String foodRate(Matcher matcher) {
        return null;
    }
    public String showFoodRate() {
        return null;
    }
    public String taxRate(Matcher matcher) {
        return null;
    }
    public String showTaxRate() {
       return null;
    }
    public String fearRate(Matcher matcher) {
        return null;
    }
    public String dropBuilding(Matcher matcher) {
        //TODO multiple command ....
        return null;
    }
    public String selectBuilding(Matcher matcher) {
        return null;
    }
    public String createUnit(Matcher matcher) {
        return null;
    }
    public String repair() {
        return null;
    }
    public String selectUnit(Matcher matcher) {
        return null;
    }
    public String moveUnit(Matcher matcher) {
        return null;
    }
    public String patrolUnit(Matcher matcher) {
        return null;
    }
    public String setUnit(Matcher matcher) {
        return null;
    }
    public String attack(Matcher matcher) {
        return null;
    }
    public String pourOil(Matcher matcher) {
        return null;
    }
    public String digTunnel(Matcher matcher) {
        return null;
    }
    public String build(Matcher matcher) {
        return null;
    }
    public String disbandUnit() {
        return null;
    }
    public String setTexture(Matcher matcher) {
        return null;
    }
    public String clear(Matcher matcher) {
        return null;
    }
    public String dropRock(Matcher matcher) {
        return null;
    }
    public String dropTree(Matcher matcher) {
        return null;
    }
    public String dropUnit(Matcher matcher) {
        return null;
    }
    public String trade(Matcher matcher) {
        return null;
    }
    public String tradeAccept(Matcher matcher) {
        return null;
    }
    public String showTradeHistory(Matcher matcher) {
        return null;
    }
    public String showPriceList(Matcher matcher) {
         return null;
    }
    public String buy(Matcher matcher) {
        return null;
    }
    public String sell(Matcher matcher) {
        return null;
    }
}
