package commands

import net.dv8tion.jda.api.events.message.MessageReceivedEvent

import scala.util.Random

class DiceCommand extends BotCommand {

  def d(event: MessageReceivedEvent): String = {
    val diceRequest = event.getMessage.getContentRaw
    val nameOfTheRoller = event.getMessage.getAuthor.getName

    val splittedCommands = diceRequest.split(" ")

    splittedCommands match {
      case listCommands if listCommands.size == 1 => imitateDice(listCommands(0), nameOfTheRoller)
      case listCommands if listCommands.size == 3 && listCommands(1) == "cap" =>
        imitateDiceWithCap(listCommands(0), nameOfTheRoller, listCommands.last.toInt)
      case _ => "***You enter wrong command***"
    }
  }

  private def imitateDice(diceRequest: String, name: String): String = {
    val splittedCommand = diceRequest.split("d")
    val (numberOfRand, maxRand) = (splittedCommand(0).toInt, splittedCommand(1).toInt)
    val listOfRandoms = List.fill(numberOfRand)(1 + Random.nextInt(maxRand))

    s"***$name rolls $diceRequest:***\n**${listOfRandoms.mkString(", ")}\nSum: ${listOfRandoms.sum}**"
  }

  private def imitateDiceWithCap(diceRequest: String, name: String, capSize: Int): String = {
    val splitted = diceRequest.split("d")
    val (numberOfRand, maxRand) = (splitted(0).toInt, splitted(1).toInt)
    val listOfRandoms = List.fill(numberOfRand)(1 + Random.nextInt(maxRand)).filter(_ > capSize)

    s"***$name rolls $diceRequest with cap $capSize:***\n**${listOfRandoms.mkString(", ")}\nSum: ${listOfRandoms.sum}**"
  }
}