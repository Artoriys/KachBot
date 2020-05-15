package commands

import net.dv8tion.jda.api.events.message.MessageReceivedEvent

import scala.util.Random

class DiceCommand extends BotCommand {

  def d(event: MessageReceivedEvent): String = {
    val diceRequest = event.getMessage.getContentRaw
    val nameOfTheRoller = event.getMessage.getAuthor.getName
    imitateDice(diceRequest, nameOfTheRoller)
  }

  private def imitateDice(diceRequest: String, name: String): String = {
    val splitted = diceRequest.split("d")
    val (numberOfRand, maxRand) = (splitted(0).toInt, splitted(1).toInt)
    val listOfRandoms = List.fill(numberOfRand)(1 + Random.nextInt(maxRand))

    s"***$name rolls $diceRequest:***\n**${listOfRandoms.mkString(", ")}\nSum: ${listOfRandoms.sum}**"
  }
}