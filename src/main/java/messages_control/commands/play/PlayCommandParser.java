package messages_control.commands.play;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayCommandParser {


    static String parseAtribute(String message) {
        Pattern commandPattern = Pattern.compile("^!(\\w+?)( (.*)?$|$)");
        Matcher commandMathcher = commandPattern.matcher(message);
        return commandMathcher.group(3);
    }
}
