package analyzer

import commands.{HelpCommand, JokeCommand, PlayCommand, WikiCommand}
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class MessageAnalyzer {

  val playCommand = new PlayCommand()
  val jokeCommand = new JokeCommand()
  val wikiCommand = new WikiCommand()
  val helpCommand = new HelpCommand()

  def analyzeEventAndMakeResponse(event: MessageReceivedEvent): String = {
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

      //Aliases and special commands
      case "gif" => "***I like static. Dynamic is shit***"
      case "oh" => "***Ah-ah***"
      case "dawai" => playCommand.add("https://www.youtube.com/watch?v=b5I4ql_XI24")
      case "marry" => "***Popins?***"

      case _ => s"***I don't know this command. Try use !help***"
    }
  }
}

object MessageAnalyzer {
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
