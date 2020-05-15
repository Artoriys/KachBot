import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory
import listener.TextListener
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity

object BotConfigurator extends Logging {
  private val textListener = new TextListener
  private val TOKEN = sys.env("BOT_TOKEN")

  def botConfigure(): JDABuilder = {
    val builder = JDABuilder.createDefault(TOKEN)
    builder.addEventListeners(textListener)
    builder.setActivity(Activity.playing("Studying DnD"))
    builder.setAudioSendFactory(new NativeAudioSendFactory())
    log.info("Bot configuration complete")
    builder
  }
}
