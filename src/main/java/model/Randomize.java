package model;

import controller.CommandParser;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;

public class Randomize {
    public static String randomCaptcha() {
        SecureRandom random = new SecureRandom();
        StringBuilder putSpace = new StringBuilder();
        String captcha;
        int length = random.nextInt(4) + 4;
        CharacterRule DR = new CharacterRule(EnglishCharacterData.Digit);
        DR.setNumberOfCharacters(length);
        PasswordGenerator passGen = new PasswordGenerator();
        captcha = passGen.generatePassword(length, DR);
        for (int i = 0; i < captcha.length(); i++) {
            if (i > 0) putSpace.append(" ");
            putSpace.append(captcha.charAt(i));
        }
        int width = 100; int height = 15;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics generator = image.getGraphics();
        generator.setFont(new Font("Arial", Font.PLAIN, 15));
        Graphics2D graphics = (Graphics2D) generator;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.drawString(putSpace.toString(), 0, 15);
        for (int y = 4; y < height; y++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int x = 0; x < width; x++)
                stringBuilder.append(image.getRGB(x, y) == -16777216 ? " " : "0");
            if (stringBuilder.toString().trim().isEmpty()) continue;
            System.out.println(stringBuilder);
        }
        CommandParser commandParser = new CommandParser();
        System.out.print("Please input the code in image : ");
        String confirmation = CommandParser.getScanner().nextLine();
        if (commandParser.validate(confirmation, "back", null) != null) return "finished";
        else if (commandParser.validate(confirmation, "change", null) != null) return randomCaptcha();
        else if (!captcha.equals(confirmation)) {System.out.println("Captcha did not match with your input!"); return randomCaptcha();}
        else {System.out.println("Verified. You are a human!"); return "done";}
    }
    public static String randomUsername(String username) {
        SecureRandom random = new SecureRandom();
        String usernameConfirmation;
        int sumOfLength = 0;
        int length = random.nextInt(2) + 2;
        sumOfLength += length;
        CharacterRule LCR = new CharacterRule(EnglishCharacterData.LowerCase);
        LCR.setNumberOfCharacters(length);
        length = random.nextInt(2) + 1;
        sumOfLength += length;
        CharacterRule UCR = new CharacterRule(EnglishCharacterData.UpperCase);
        UCR.setNumberOfCharacters(length);
        length = random.nextInt(2) + 1;
        sumOfLength += length;
        CharacterRule DR = new CharacterRule(EnglishCharacterData.Digit);
        DR.setNumberOfCharacters(length);
        PasswordGenerator passGen = new PasswordGenerator();
        username = username + passGen.generatePassword(sumOfLength, LCR, UCR, DR);
        System.out.println("Your random username is : " + username);
        System.out.print("Please re-enter your username here : ");
        usernameConfirmation = CommandParser.getScanner().nextLine();
        if (!usernameConfirmation.equals(username)) {System.out.println("Username did not match with username confirmation!"); return null;}
        else return username;
    }

    public static String randomPassword() {
        SecureRandom random = new SecureRandom();
        String passwordConfirmation;
        int sumOfLength = 0;
        int length = random.nextInt(4) + 2;
        sumOfLength += length;
        CharacterRule LCR = new CharacterRule(EnglishCharacterData.LowerCase);
        LCR.setNumberOfCharacters(length);
        length = random.nextInt(4) + 2;
        sumOfLength += length;
        CharacterRule UCR = new CharacterRule(EnglishCharacterData.UpperCase);
        UCR.setNumberOfCharacters(length);
        length = random.nextInt(4) + 1;
        sumOfLength += length;
        CharacterRule DR = new CharacterRule(EnglishCharacterData.Digit);
        DR.setNumberOfCharacters(length);
        length = random.nextInt(4) + 1;
        sumOfLength += length;
        CharacterRule SR = new CharacterRule(EnglishCharacterData.Special);
        SR.setNumberOfCharacters(length);
        PasswordGenerator passGen = new PasswordGenerator();
        String password = passGen.generatePassword(sumOfLength, SR, LCR, UCR, DR);
        System.out.println("Your random password is: " + password);
        System.out.print("Please re-enter your password here: ");
        passwordConfirmation = CommandParser.getScanner().nextLine();
        if (!passwordConfirmation.equals(password)) {System.out.println("Password did not match with password confirmation!"); return null;}
        return password;
    }

    public static String randomSlogan() {
        ArrayList<String> slogans = new ArrayList<>(Arrays.asList(
                "We Quell the Storm, and Ride the Thunder.",
                "Better to die than to be a coward.",
                "My destination is beyond everyone else’s destination",
                "War does not determine who is right - only who is left.",
                "The more you sweat in peace, the less you bleed in war.",
                "Know thy self, know thy enemy. A thousand battles, a thousand victories.",
                "If there were no wars, how could we know the value of peace?",
                "Live for something rather than die for nothing.",
                "No Better Friend, No Worse Enemy"));
        SecureRandom random = new SecureRandom();
        String chosenSlogan = slogans.get(random.nextInt(9));
        System.out.println("Your slogan is : " + chosenSlogan);
        return chosenSlogan;
    }
}
