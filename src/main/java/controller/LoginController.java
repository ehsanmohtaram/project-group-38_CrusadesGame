package controller;

import model.Randomize;
import model.User;
import org.apache.commons.codec.digest.DigestUtils;
import java.util.*;

public class LoginController {

    public String login(HashMap<String, String> options) {
        if (options.get("A").equals("-")) {
            if (User.getUserByUsername(options.get("u")) == null) return "Username and password did not match!";
        }
        if (User.getStatueOfDelayOfUser(options.get("u")).equals(true))
            return "You can not login for now. Please try in " +
                    (User.getTimeOfDelayOfUser(options.get("u")) - setDelay(options.get("u"))) + " seconds!";
        if (options.get("A").equals("-")) {
            if (!User.getUserByUsername(options.get("u")).checkPassword(options.get("p"))) {
                setDelay(options.get("u"));
                return "Username and password did not match!";
            }
        }
        if (!options.get("C").equals(options.get("c"))) return "Security field failed! Please try again.";
        if (options.get("A").equals("*")) {
            if (!options.get("a").equals(User.getUserByUsername(options.get("u")).getAnswerToSecurityQuestion()))
                return "Your answer did not match with the primary answer!";
        }
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

}
