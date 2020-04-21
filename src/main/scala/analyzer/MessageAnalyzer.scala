package analyzer

import commands.{JokeCommand, WikiCommand}

class MessageAnalyzer {

  def analyzeMessageAndMakeResponse(command: String): String = {
    val subCommands = command.split(" ").toList

    subCommands.head.substring(1) match {
      case "gif" => "***I like static. Dynamic is shit***"
      case "oh" => "***Ah-ah***"
      case "joke" => new JokeCommand().getRandomJoke
      case "wiki" => new WikiCommand().searchAndMakeResponse(subCommands.drop(1))
      case _ => s"***I don't know this command. Try on of these: \n${analyzer.knownCommands.mkString("\n")}***"
    }
  }
}

object MessageAnalyzer {
  def isCommandToBot(command: String): Boolean = {
    command.startsWith("!") && command.length > 1
  }
}
