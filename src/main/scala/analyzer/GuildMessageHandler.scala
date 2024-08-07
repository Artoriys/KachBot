package analyzer

import commands.{HelpCommand, JokeCommand, PlayCommand, WikiCommand}
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class GuildMessageHandler {

  val playCommand = new PlayCommand()
  val jokeCommand = new JokeCommand()
  val wikiCommand = new WikiCommand()
  val helpCommand = new HelpCommand()

  def handleEventAndMakeResponse(event: MessageReceivedEvent): String = {
    Message.suppressContentIntentWarning()
    val command = event.getMessage.getContentRaw
    val subCommands = command.split(" ").toList

    subCommands.head.substring(1) match {
      //General commands
      case "help" => helpCommand.help()

      //Joke commands
      case "joke" => jokeCommand.joke()

      //Wiki commands
      case "w" => wikiCommand.w(subCommands.drop(1))

      //Audio play commands
      case "add" => playCommand.add(subCommands.last)
      case "join" => playCommand.join(event)
      case "pause" => playCommand.pause()
      case "unpause" => playCommand.unpause()
      case "leave" => playCommand.leave(event)
      case "list" => playCommand.list()
      case "volume" => playCommand.volume(subCommands.last)
      case "skip" => playCommand.skip()
      case "reset" => playCommand.reset(event)
      case "shuffle" => playCommand.shuffle()
      case "down" => playCommand.down(event)
      case "np" => playCommand.np()
      case "pos" => playCommand.pos(subCommands.last)
      case "tt" => playCommand.tt()

      //Aliases and special commands
      case "gif" => "***I like static. Dynamic is shit***"
      case "oh" => "***Ah-ah***"
      case "dawai" => playCommand.add("https://www.youtube.com/watch?v=b5I4ql_XI24")
      case "marry" => "***Popins?***"
      case "kawai" => playCommand.add("https://www.youtube.com/watch?v=9lNZ_Rnr7Jc")

      case _ => s"***I don't know this command. Try use !help***"
    }
  }
}

object GuildMessageHandler {
  def isCommandToBot(event: MessageReceivedEvent): Boolean = {
    val command = event.getMessage.getContentRaw
    command.startsWith("!") && command.length > 1
  }

  def isDiceRollCommand(event: MessageReceivedEvent): Boolean = {
    val command = event.getMessage.getContentRaw
    val diceRegEx = "^(\\d+d\\d+)($|\\s.*$)".r
    diceRegEx.matches(command)
  }
}
