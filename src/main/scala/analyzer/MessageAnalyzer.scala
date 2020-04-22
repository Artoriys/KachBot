package analyzer

import commands.{JokeCommand, PlayCommand, WikiCommand}
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class MessageAnalyzer {

  def analyzeEventAndMakeResponse(event: MessageReceivedEvent): String = {
    val command = event.getMessage.getContentRaw
    val subCommands = command.split(" ").toList

    subCommands.head.substring(1) match {
      case "gif" => "***I like static. Dynamic is shit***"
      case "oh" => "***Ah-ah***"
      case "joke" => new JokeCommand().getRandomJoke
      case "wiki" => new WikiCommand().searchAndMakeResponse(subCommands.drop(1))
      case "play" => new PlayCommand().analyzeSubCommandsAndMakeResponse(subCommands.drop(1), event)
      case "leave" => event.getGuild.getAudioManager.closeAudioConnection()
        "I'm leaving. Bye!"
      case _ => s"***I don't know this command. Try on of these: \n${analyzer.knownCommands.mkString("\n")}***"
    }
  }
}

object MessageAnalyzer {
  def isCommandToBot(event: MessageReceivedEvent): Boolean = {
    val command = event.getMessage.getContentRaw
    command.startsWith("!") && command.length > 1
  }
}
