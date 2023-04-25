package controller;

import model.Randomize;
import model.User;
import org.apache.commons.codec.digest.DigestUtils;
import view.ProfileMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileController {
    private final User currentUser;
    private final ProfileMenu profileMenu;
    public ProfileController() {
        this.profileMenu = new ProfileMenu(this);
        currentUser = Controller.currentUser;
    }

    public String run(){
        return profileMenu.run();
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
}
