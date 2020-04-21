object Main extends Logging {
  def main(args: Array[String]): Unit = {
    BotConfigurator.botConfigure().build()
    log.info("Bot started")
  }
}
