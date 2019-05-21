package dbLogAddon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.LocalDateTime;

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
        this.localDateTime = event.getMessage().getCreationTime().toLocalDateTime();
        this.message = event.getMessage().getContentDisplay();
        this.channel = event.getChannel().getName();
    }
}
