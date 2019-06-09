package messages_control.commands.wiki;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiCommandParser {


    public static String parse(String message) {
        String wordTofind;
        Pattern commandPattern = Pattern.compile("^!(\\w+?)( (.*)?$|$)");
        Matcher commandMathcher = commandPattern.matcher(message);

        wordTofind = commandMathcher.group(3);
        WikiCommand wikiCommand = new WikiCommand();
        return wikiCommand.getLinkToWikiByMessage(wordTofind);
    }

}
