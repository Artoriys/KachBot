package commands

import org.json4s.{DefaultFormats, JArray}

import scala.io.Source
import org.json4s.native.JsonMethods.parse

class WikiCommand {

  private val searchQuery = "https://%.wikipedia.org/w/api.php?action=opensearch&namespace=0&limit=1&format=json&search=%"
  private val NAME_TYPE = 1
  private val URL_TYPE = 3

  def searchAndMakeResponse(subcommands: List[String]): String = {
    val searchStr = makeSearchString(subcommands)

    if (("([А-Яа-я0-9ё_ ]*)".r findFirstIn searchStr).isDefined) {
      makeRequestAndParseResponse(searchQuery.format("ru", searchStr))
    } else {
      makeRequestAndParseResponse(searchQuery.format("en", searchStr))
    }
  }

  private def makeSearchString(subcommands: List[String]): String =
    subcommands.mkString("_")

  private def makeRequestAndParseResponse(query: String): String = {
    val buffer = Source.fromURL(query)
    val urlContent = buffer.mkString("")
    buffer.close()

    val name = parseResponse(urlContent, NAME_TYPE)
    val url = parseResponse(urlContent, URL_TYPE)

    name match {
      case Some(value) => s"Hooray!\nI found something for you\n$value\n${url.getOrElse("")}"
      case None => "Oh, God! Shitty Wiki don't give me what you want.\nIt's disgusting"
    }
  }

  private def parseResponse(response: String, typeOfResponse: Int): Option[String] = {
    implicit val formats: DefaultFormats = DefaultFormats
    parse(response).asInstanceOf[JArray].arr(typeOfResponse).extract[List[String]].headOption
  }
}
