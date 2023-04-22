package controller;

import com.sanityinc.jargs.CmdLineParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser{
    private static final Scanner scanner = new Scanner(System.in);
    public HashMap<String, String> validate (String inputCommand, String mainCommand, String options) {
        ArrayList<String> commandParser = new ArrayList<>();
        ArrayList<CmdLineParser.Option<String>> optionsParserSave = new ArrayList<>();
        HashMap<String, String> valueSetter = new HashMap<>();
        CmdLineParser parser = new CmdLineParser();
        inputCommand = inputCommand.trim();
        if (mainCommandChecker(inputCommand, mainCommand, options) == null)  return null;
        inputCommand = mainCommandChecker(inputCommand, mainCommand, options);
        if (options != null) {
            for (String key : optionParser(options.replaceAll("\\?","")).keySet())
                optionsParserSave.add(parser.addStringOption(key.charAt(0), optionParser(options.replaceAll("\\?","")).get(key)));
            Matcher matcher = Pattern.compile("\\S+").matcher(inputCommand);
            int counter = 0;
            while (matcher.find()) {
                if (matcher.start() > checkForQuotEndPosition(inputCommand).get(counter) &&
                        checkForQuotContent(inputCommand).size() - 1 > counter)
                    counter++;
                if (checkForQuotStartPosition(inputCommand).get(counter) > matcher.start() ||
                        checkForQuotEndPosition(inputCommand).get(counter) < matcher.end())
                    commandParser.add(matcher.group());
                if (matcher.start() == checkForQuotStartPosition(inputCommand).get(counter))
                    commandParser.add(checkForQuotContent(inputCommand).get(counter));
            }
            for (int i = 0; i < commandParser.size(); i++) {
                if (i < commandParser.size() - 1 &&
                        optionParser(options).containsKey(commandParser.get(i).replaceAll("-", "")) &&
                                optionParser(options).containsKey(commandParser.get(i+1).replaceAll("-", "")))
                    commandParser.add(i+1,"");
            }
            if (towFactorDetector(options).size() != 0) {
                for (String key : towFactorDetector(options).keySet())
                    for (int i = 0; i < commandParser.size(); i++)
                        if (key.equals(commandParser.get(i).replaceAll("-", "")) && i < commandParser.size() - 2)
                            if (!optionParser(options).containsKey(commandParser.get(i + 1).replaceAll("-", "")) &&
                                    !optionParser(options).containsKey(commandParser.get(i + 2).replaceAll("-", "")) &&
                                    !optionParser(options).containsValue(commandParser.get(i + 1).replaceAll("-", "")) &&
                                    !optionParser(options).containsValue(commandParser.get(i + 2).replaceAll("-", ""))) {
                                valueSetter.put(key.toUpperCase(), commandParser.get(i + 2)); commandParser.remove(i+2);
                            }
            }
            String[] splitText = new String[commandParser.size()];
            splitText = commandParser.toArray(splitText);
            try { parser.parse(splitText);}
            catch (CmdLineParser.OptionException check) {
                System.out.println(check.getMessage());
                if (check.getMessage().matches("^Unknown option.*") || check.getMessage().matches("^Illegal option.*")) return null;
            }
            try {if (parser.getRemainingArgs().length != 0 && !parser.getRemainingArgs()[0].equals(""))  return null;}
            catch (NullPointerException ignored) {}
            counter = 0;
            options = options.replaceAll("\\?","");
            for (String key : optionParser(options).keySet()) {
                valueSetter.put(key, parser.getOptionValue(optionsParserSave.get(counter)));
                counter++;
            }
        }
        else valueSetter.put(null, null);
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
        if (argPositionStart.size() == 0) argPositionStart.add(-1);
        return argPositionStart;
    }

    public ArrayList<Integer> checkForQuotEndPosition(String inputCommand) {
        ArrayList<Integer> argPositionEnd = new ArrayList<>();
        Matcher matcher = Pattern.compile("\"([^\"]*)\"").matcher(inputCommand);
        while (matcher.find()) argPositionEnd.add(matcher.end());
        if (argPositionEnd.size() == 0) argPositionEnd.add(-1);
        return argPositionEnd;
    }

    public HashMap<String, String> optionParser(String options) {
        HashMap<String, String> optionDetect = new HashMap<>();
        String[] longAndShortOptionDetect = options.split("/");
        for (String singleOption : longAndShortOptionDetect) {
            if (!singleOption.contains("?")) {
                if (singleOption.split("\\|").length == 2)
                    optionDetect.put(singleOption.split("\\|")[0], singleOption.split("\\|")[1]);
                else optionDetect.put(singleOption.split("\\|")[0], null);
            }
        }
        return optionDetect;
    }

    public HashMap<String, String> towFactorDetector(String options) {
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

    public String mainCommandChecker(String inputCommand, String mainCommand, String option) {
        if (option != null) mainCommand = "^\\s*" + mainCommand.replaceAll(" ", "\\\\s+") + ".*";
        else mainCommand = "^\\s*" + mainCommand.replaceAll(" ", "\\\\s+") + "$";
        if (!inputCommand.matches(mainCommand)) return null;
        mainCommand = mainCommand.replaceAll("\\.\\*","");
        inputCommand = inputCommand.replaceAll(mainCommand, "");
        return inputCommand.trim();
    }

    public static Scanner getScanner() {
        return scanner;
    }
}