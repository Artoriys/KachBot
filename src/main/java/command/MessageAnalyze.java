package command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MessageAnalyze {

    private String message;
    private Pattern commandPattern;
    private Matcher commandMathcher;
    private String commandAttribute;

    MessageAnalyze(String message) {
        this.message = message;
        commandPattern = Pattern.compile("^!(\\w+?)( (.*)?$|$)");
        commandMathcher = commandPattern.matcher(this.message);

    }

    String makeMessage() {
        return analyzeMessage();
    }

    private String analyzeMessage() {
        String result = "";

        if (commandMathcher.matches()) {
            commandAttribute = commandMathcher.group(3);
            result = chooseCommand(commandMathcher.group(1));
        }

        return result;

    }

    private String chooseCommand(String command) {
        String result = "I don't know this command";
        switch (command) {
            case "gif" : result = "I don't have gifs"; break;

            case "oh" : result = "ahh"; break;

            case "joke" : result = "Your mam is fat!!"; break;

            case "wiki" :
                WikiCommand wikiCommand = new WikiCommand(commandAttribute);
                result = wikiCommand.getLinkToWikiByMessage();
            break;
        }

        return result;
    }

    static boolean isCommandToBot(String message) {
        Pattern command = Pattern.compile("!.+?");
        Matcher commandMathcher = command.matcher(message);
        return commandMathcher.matches();
    }



}
