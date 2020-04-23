package commands

import scala.io.Source

class HelpCommand extends BotCommand {

  def help(): String =
    Source.fromResource("help.txt").mkString("")
}
