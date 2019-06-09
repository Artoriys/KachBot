package messages_control;

import messages_control.commands.joke.JokeCommand;
import messages_control.commands.play.PlayCommand;
import messages_control.commands.wiki.WikiCommandParser;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MessageAnalyze {

    private String message;
    private Matcher commandMathcher;
    private MessageReceivedEvent event;

    MessageAnalyze(MessageReceivedEvent event) {
        this.event = event;
        this.message = event.getMessage().getContentRaw();
        Pattern commandPattern = Pattern.compile("^!(\\w+?)( (.*)?$|$)");
        commandMathcher = commandPattern.matcher(this.message);
    }

    static boolean isCommandToBot(String message) {
        Pattern command = Pattern.compile("!.+?");
        Matcher commandMathcher = command.matcher(message);
        return commandMathcher.matches();
    }

    String makeMessage() {
        return analyzeMessage();
    }

    private String analyzeMessage() {
        String result = "";
        if (commandMathcher.matches()) {
            result = chooseCommand(commandMathcher.group(1));
        }

        return result;

    }

    private String chooseCommand(String command) {
        String result = "I don't know this command :(";
        switch (command) {
            case "gif":
                result = "I don't have gifs";
                break;

            case "oh":
                result = "ahh";
                break;

            case "play":
                PlayCommand playCommand = new PlayCommand(event);
                playCommand.play();
                break;

            case "joke":
                result = new JokeCommand().getJoke();
                break;

            case "wiki":
                result = WikiCommandParser.parse(message);
                break;
        }
        return result;
    }

}
