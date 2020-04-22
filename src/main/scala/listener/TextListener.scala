package listener

import analyzer.MessageAnalyzer
import dbLogAddon.handlers.LogRepositoryImpl
import dbLogAddon.model.Message
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class TextListener extends ListenerAdapter {
  private val logRepository = new LogRepositoryImpl
  private val msgAnalyzer = new MessageAnalyzer()

  override def onMessageReceived(event: MessageReceivedEvent): Unit = {
    if (!event.getAuthor.isBot) {

      //Maybe we don't need it anymore
      //logRepository.saveMassage(new Message(event))

      if (MessageAnalyzer.isCommandToBot(event)) {
        val response = msgAnalyzer.analyzeEventAndMakeResponse(event)

        event.getChannel.sendMessage(response).queue()
      }
    }
  }
}
