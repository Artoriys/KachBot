package commands

import org.json4s.DefaultFormats

import scala.io.{Codec, Source}
import org.json4s.native.JsonMethods.parse

class JokeCommand {

  private val url = "https://official-joke-api.appspot.com/random_joke"


  def getRandomJoke: String = {
    implicit val codec: Codec = Codec.UTF8

    val buffer = Source.fromURL(url)
    val urlContent = buffer.mkString("")
    buffer.close()

    val setup = extractJsonField(urlContent, "setup")
    val punchline = extractJsonField(urlContent, "punchline")

    s"***$setup \n\n $punchline***"
  }


  private def extractJsonField(urlContent: String, field: String): String = {
    implicit val formats: DefaultFormats = DefaultFormats
    (parse(urlContent) \ field).extract[String]
  }
}
