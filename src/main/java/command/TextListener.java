package command;

import lombok.extern.log4j.Log4j;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

@Log4j(topic = "message")
public class TextListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String command;

        if (event.getAuthor().isBot()) {
            return;
        }
        log.info(event.getAuthor().getName()
                + " : "
                + event.getMessage().getContentDisplay());

        command = event.getMessage().getContentRaw();
        if (MessageAnalyze.isCommandToBot(command)) {
            MessageAnalyze analyze = new MessageAnalyze(command);
            event.getChannel().sendMessage(analyze.makeMessage()).queue();
        }

    }




}



