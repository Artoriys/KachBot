package listener

import analyzer.GuildMessageHandler
import commands.DiceCommand
import dbLogAddon.handlers.LogRepositoryImpl
import dbLogAddon.model.Message
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

import scala.collection.mutable

class TextListener extends ListenerAdapter {
  private val logRepository = new LogRepositoryImpl
  private val guildsMap = mutable.Map.empty[Guild, GuildMessageHandler]

  override def onMessageReceived(event: MessageReceivedEvent): Unit = {
    if (!event.getAuthor.isBot) {

      //Maybe we don't need it anymore
      //logRepository.saveMassage(new Message(event))

      val msgAnalyzer = pickGuildMessageHandler(event.getGuild)

      event match {
        case ev if GuildMessageHandler.isCommandToBot(ev) =>
          val response = msgAnalyzer.handleEventAndMakeResponse(event)
          event.getChannel.sendMessage(response).queue()

        case ev if GuildMessageHandler.isDiceRollCommand(ev) =>
          val response = new DiceCommand().d(ev)
          event.getChannel.sendMessage(response).queue()

        case _ => ()
      }
    }
  }

  private def pickGuildMessageHandler(guild: Guild): GuildMessageHandler =
    guildsMap.get(guild) match {
      case Some(value) => value
      case None =>
        val guildMH = new GuildMessageHandler()
        guildsMap.addOne(guild, guildMH)
        guildMH
    }
}
