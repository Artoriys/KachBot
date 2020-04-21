import listener.TextListener
import net.dv8tion.jda.core.entities.Game
import net.dv8tion.jda.core.{AccountType, JDABuilder}

object BotConfigurator extends Logging {
  private val textListener = new TextListener
  private val builder = new JDABuilder(AccountType.BOT)
  private val TOKEN = sys.env("BOT_TOKEN")

  def botConfigure(): JDABuilder = {
    builder.setToken(TOKEN)
    builder.addEventListener(textListener)
    builder.setGame(Game.playing("Arma 3: Tushino Edition"))
    log.info("Bot configuration complete")
    builder
  }
}
