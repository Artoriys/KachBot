package listener

import analyzer.MessageAnalyzer
import dbLogAddon.handlers.LogRepositoryImpl
import dbLogAddon.model.Message
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class TextListener extends ListenerAdapter {
  private val logRepository = new LogRepositoryImpl

  override def onMessageReceived(event: MessageReceivedEvent): Unit = {
    if (!event.getAuthor.isBot) {

      logRepository.saveMassage(new Message(event))

      //val command = event.getMessage.getContentRaw

      if (MessageAnalyzer.isCommandToBot(event)) {
        val response = new MessageAnalyzer().analyzeEventAndMakeResponse(event)

        event.getChannel.sendMessage(response).queue()
      }
    }
  }
}
