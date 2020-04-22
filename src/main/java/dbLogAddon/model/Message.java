package dbLogAddon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.Message.Attachment;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Message {
    private String user;
    private LocalDateTime localDateTime;
    private String message;
    private String channel;

    public Message(MessageReceivedEvent event){
        this.user = event.getAuthor().getName();
        this.localDateTime = event.getMessage().getTimeCreated().toLocalDateTime();
        this.message = checkForAttachments(event);
        this.channel = event.getChannel().getName();
    }

    private String checkForAttachments(MessageReceivedEvent event) {
        String result = event.getMessage().getContentDisplay();
        List<Attachment> attachments = event.getMessage().getAttachments();
        if (result.equals("") && !attachments.isEmpty()) {
            result = attachments.get(0).getUrl();
        }
        return result;
    }
}
