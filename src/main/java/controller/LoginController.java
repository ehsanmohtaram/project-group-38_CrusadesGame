package controller;

import model.Randomize;
import model.User;
import org.apache.commons.codec.digest.DigestUtils;
import java.util.*;

public class LoginController {

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


//    public String forgetPassword(HashMap<String, String> options) {
//        if (options.get("u") == null) return "Please input necessary options!";
//        if (options.get("u").equals("")) return "Illegal value. Please fill the options!";
//        System.out.println("Please answer the following question : ");
//        System.out.print(User.questions.get(User.getUserByUsername(options.get("u")).getSecurityQuestionNumber()) + " ");
//        String confirm;
//        confirm = CommandParser.getScanner().nextLine();
//        if (!confirm.equals(User.getUserByUsername(options.get("u")).getAnswerToSecurityQuestion())) return "Your answer did not match!";
//        String inputCommand = CommandParser.getScanner().nextLine();
//        CommandParser commandParser = new CommandParser();
//        System.out.println("Set your new password like this : new password -p <password> <password confirmation>");
//        HashMap<String, String> optionPass = commandParser.validate(inputCommand, "new password","?p|password");
//        String password;
//        if ((password = passwordValidation(optionPass.get("p"),optionPass.get("P"))) == null) return "Password change failed!";
//        User.getUserByUsername(options.get("u")).setPassword(DigestUtils.sha256Hex(password));
//        return "Password changed successfully";
//    }

}
