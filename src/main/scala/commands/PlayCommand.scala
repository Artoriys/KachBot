package commands

import audio.{KachBotAudioEventAdapter, KachBotAudioResultHandler, KachBotAudioSendHandler}
import com.sedmelluq.discord.lavaplayer.player.{AudioPlayer, DefaultAudioPlayerManager}
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.managers.AudioManager

import scala.jdk.CollectionConverters._

class PlayCommand {

  val audioEventAdapter = new KachBotAudioEventAdapter()
  val playerManager = new DefaultAudioPlayerManager()

  AudioSourceManagers.registerRemoteSources(playerManager)

  val audioPlayer: AudioPlayer = playerManager.createPlayer
  audioPlayer.addListener(audioEventAdapter)
  var audioManager:AudioManager = _
  val resultHandler = new KachBotAudioResultHandler(audioPlayer)
  val sendHandler = new KachBotAudioSendHandler(audioPlayer)



  def analyzeSubCommandsAndMakeResponse(subCommands: List[String], event: MessageReceivedEvent): String = {
    subCommands.head match {
      case "add" => "add to queue"
      case "start" => play(event, subCommands.last)
      case "pause" => pause()
      case "unpause" => unpause()
      case "stop" => "delete queue and quite channel"
    }
  }


  def play(event: MessageReceivedEvent, url: String) = {
    val guild = event.getGuild
    val voiceChannel = pickVoiceChannel(event)

    playerManager.loadItem(url, resultHandler)

    audioManager = guild.getAudioManager
    audioManager.setSendingHandler(sendHandler)
    audioManager.openAudioConnection(voiceChannel)
    "***I'll play this sweet-sweet music for you***"
  }

  def pause() = {
    audioPlayer.setPaused(true)
    "***Pausing your track***"
  }

  def unpause(): String = {
    audioPlayer.setPaused(false)
    "***Unpausing your track***"
  }




  private def pickVoiceChannel(event: MessageReceivedEvent): VoiceChannel = {
    val guild = event.getGuild
    val channels = guild.getVoiceChannels.asScala.toList

    channels.find(vc => !vc.getMembers.isEmpty) match {
      case Some(value) => value
      case None => channels.head
    }
  }

}
