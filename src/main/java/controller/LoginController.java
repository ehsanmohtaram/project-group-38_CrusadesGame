package controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Randomize;
import model.User;
import org.apache.commons.codec.digest.DigestUtils;
import view.LoginMenu;

import java.util.*;

public class LoginController {
    public LoginController() {
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
        ImageView avatar = new ImageView(new Image(LoginController.class.getResource("/images/avatars/1.jpg").toExternalForm()));
        User.addUser(username,nikName,password,email,slogan,Integer.parseInt(optionPass.get("q")) - 1,optionPass.get("a"), avatar);

        return "User has been added successfully!";
    }
    public String login(HashMap<String, String> options) {
        if (User.getUserByUsername(options.get("u")) == null) return "Username and password did not match!";
        if (User.getStatueOfDelayOfUser(options.get("u")).equals(true))
            return "You can not login for now. Please try in " +
                    (User.getTimeOfDelayOfUser(options.get("u")) - setDelay(options.get("u"))) + " seconds!";
        if (!User.getUserByUsername(options.get("u")).checkPassword(options.get("p"))) {
            setDelay(options.get("u"));
            return "Username and password did not match!";
        }
        if (!options.get("C").equals(options.get("c"))) return "Security field failed! Please try again.";
        if (options.get("s") != null) User.getUserByUsername(options.get("u")).setLoggedIn(true);
        Controller.currentUser = Controller.loggedInUser = User.getUserByUsername(options.get("u"));
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

}
