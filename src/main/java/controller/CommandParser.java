package controller;

import com.sanityinc.jargs.CmdLineParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser {
    private static final Scanner scanner = new Scanner(System.in);
    public HashMap<String, String> validate(String inputCommand, String mainCommand, String options) {
        ArrayList<String> commandParser = new ArrayList<>();
        ArrayList<CmdLineParser.Option<String>> optionsParser = new ArrayList<>();
        HashMap<String, String> valueSetter = new HashMap<>();
        CmdLineParser parser = new CmdLineParser();
        if (mainCommandChecker(inputCommand, mainCommand) == null) return null;
        inputCommand = mainCommandChecker(inputCommand, mainCommand);
        for (String key : optionParser(options).keySet())
            optionsParser.add(parser.addStringOption(key.charAt(0), optionParser(options).get(key)));
        if (desireOptionParser(options).size() != 0)
            for (String key : desireOptionParser(options).keySet())
                optionsParser.add(parser.addStringOption(key.charAt(0), desireOptionParser(options).get(key)));
        inputCommand = inputCommand.trim();
        Matcher matcher = Pattern.compile("\\S+").matcher(inputCommand);
        int counter = 0;
        if (checkForQuotContent(inputCommand).size() != 0) {
            while (matcher.find()) {
                if (matcher.start() == checkForQuotStartPosition(inputCommand).get(counter))
                    commandParser.add(checkForQuotContent(inputCommand).get(counter));
                else if (matcher.end() == checkForQuotEndPosition(inputCommand).get(counter) &&
                        checkForQuotContent(inputCommand).size() - 1 > counter)
                    counter++;
                else if (checkForQuotStartPosition(inputCommand).get(counter) > matcher.start() ||
                        checkForQuotEndPosition(inputCommand).get(counter) < matcher.end())
                    commandParser.add(matcher.group());
            }
        }
        String[] splitText = new String[commandParser.size()];
        splitText = commandParser.toArray(splitText);
        try {
            if (checkForQuotContent(inputCommand).size() != 0) parser.parse(splitText);
            else parser.parse(inputCommand.split("\\s+"));
        }
        catch (CmdLineParser.OptionException ignored) {}
        try {
            if (parser.getRemainingArgs().length != 0) return null;
        }
        catch (NullPointerException ignored) {}
        counter = 0;
        for (String key : optionParser(options.replaceAll("\\?","")).keySet()) {
            valueSetter.put(key, parser.getOptionValue(optionsParser.get(counter)));
            counter++;
        }
        counter = 0;
        for (String key : valueSetter.keySet())
            if (valueSetter.get(key) == null && !desireOptionParser(options).containsKey(key)) {
                System.out.println("Your " + optionParser(options).get(key) + " field is empty!");
                counter++;
            }
        if (counter != 0) return null;
        return valueSetter;
    }
    public ArrayList<String> checkForQuotContent(String inputCommand) {
        ArrayList<String> commandParserQuote = new ArrayList<>();
        Matcher matcher = Pattern.compile("\"([^\"]*)\"").matcher(inputCommand);
        while (matcher.find()) commandParserQuote.add(matcher.group(1));
        return commandParserQuote;
    }

    public ArrayList<Integer> checkForQuotStartPosition(String inputCommand) {
        ArrayList<Integer> argPositionStart = new ArrayList<>();
        Matcher matcher = Pattern.compile("\"([^\"]*)\"").matcher(inputCommand);
        while (matcher.find()) argPositionStart.add(matcher.start());
        return argPositionStart;
    }

    public ArrayList<Integer> checkForQuotEndPosition(String inputCommand) {
        ArrayList<Integer> argPositionEnd = new ArrayList<>();
        Matcher matcher = Pattern.compile("\"([^\"]*)\"").matcher(inputCommand);
        while (matcher.find()) argPositionEnd.add(matcher.end());
        return argPositionEnd;
    }

    public HashMap<String, String> optionParser(String options) {
        HashMap<String, String> optionDetect = new HashMap<>();
        String[] longAndShortOptionDetect = options.split("/");
        for (String singleOption : longAndShortOptionDetect) {
            if (singleOption.split("\\|").length == 2)
                optionDetect.put(singleOption.split("\\|")[0], singleOption.split("\\|")[1]);
            else optionDetect.put(singleOption.split("\\|")[0], null);
        }
        return optionDetect;
    }

    public HashMap<String, String> desireOptionParser(String options) {
        HashMap<String, String> desireOptionDetect = new HashMap<>();
        String[] longAndShortOptionDetect = options.split("/");
        for (String singleOption : longAndShortOptionDetect) {
            if (singleOption.contains("?")) {
                singleOption = singleOption.replaceAll("\\?","");
                if (singleOption.split("\\|").length == 2)
                    desireOptionDetect.put(singleOption.split("\\|")[0], singleOption.split("\\|")[1]);
                else desireOptionDetect.put(singleOption.split("\\|")[0], null);
            }
        }
        return desireOptionDetect;
    }

    public String mainCommandChecker(String inputCommand, String mainCommand) {
        mainCommand = "^\\s*" + mainCommand.replaceAll(" ", "\\\\s+") + "\\s.*";
        if (!inputCommand.matches(mainCommand)) return null;
        mainCommand = mainCommand.replaceAll("\\.\\*","");
        inputCommand = inputCommand.replaceAll(mainCommand, "");
        return inputCommand.trim();
    }

    public static Scanner getScanner() {
        return scanner;
    }

}