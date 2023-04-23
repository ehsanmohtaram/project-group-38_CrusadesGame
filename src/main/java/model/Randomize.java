package model;

import controller.CommandParser;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;

public class Randomize {
    public static String randomKapcha() {
        return null;
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
