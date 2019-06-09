package messages_control;

import dbLogAddon.handlers.LogRepositoryImpl;
import dbLogAddon.model.Message;
import lombok.extern.log4j.Log4j;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

@Log4j(topic = "file")
public class TextListener extends ListenerAdapter {
    private LogRepositoryImpl logRepository = new LogRepositoryImpl();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String command;
        if (event.getAuthor().isBot()) {
            return;
        }
        logRepository.saveMassage(new Message(event));
        command = event.getMessage().getContentRaw();

        if (MessageAnalyze.isCommandToBot(command)) {
            MessageAnalyze analyze = new MessageAnalyze(event);
            event.getChannel().sendMessage(analyze.makeMessage()).queue();
        }
    }
}



