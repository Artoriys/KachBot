package commands

import audio.KachBotMusicManager
import com.sedmelluq.discord.lavaplayer.player.{AudioLoadResultHandler, AudioPlayer, DefaultAudioPlayerManager}
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.{AudioPlaylist, AudioTrack}
import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

import scala.jdk.CollectionConverters._
import scala.util.Random

class PlayCommand {

  val playerManager: DefaultAudioPlayerManager = new DefaultAudioPlayerManager()
  val kachMng = new KachBotMusicManager(playerManager)
  val player: AudioPlayer = kachMng.audioPlayer

  AudioSourceManagers.registerRemoteSources(playerManager)

  def join(event: MessageReceivedEvent): String = {
    val guild = event.getGuild
    val voiceChannel = pickVoiceChannel(event)
    val manager = guild.getAudioManager

    manager.setSendingHandler(kachMng.sendHandler)
    manager.openAudioConnection(voiceChannel)

    s"***Joined to ${voiceChannel.getName} and ready to play this sweet-sweet music for you***"
  }

  def add(url: String): String = {
    playerManager.loadItemOrdered(kachMng, url, new KachLoadResultHandler())
    s"***Track added in queue \n Now we have ${kachMng.scheduler.queue.length + 1} tracks***"
  }

  def pause(): String = {
    player.setPaused(true)
    "***Pausing your track***"
  }

  def unpause(): String = {
    player.setPaused(false)
    "***Unpausing your track***"
  }

  def leave(event: MessageReceivedEvent): String = {
    event.getGuild.getAudioManager.setSendingHandler(null)
    event.getGuild.getAudioManager.closeAudioConnection()
    "***I'm leaving your voice channel. Keep your secrets!***"
  }

  def list(): String = {
    np() +
      s"\n\n***Queue contains ${kachMng.scheduler.queue.length} tracks \n\n" +
      s"List of tracks:\n ${kachMng.scheduler.queue.map(_.getInfo.title).mkString("\n")}***"
  }

  def skip(): String = {
    val playingTrackTitle = kachMng.scheduler.audioPlayer.getPlayingTrack.getInfo.title
    kachMng.scheduler.nextTrack()
    s"***Skipping \n$playingTrackTitle***"
  }

  def volume(vol: String): String = {
    val parsedVolume = math.max(10, math.min(100, vol.toInt))
    kachMng.audioPlayer.setVolume(parsedVolume)
    s"***Volume set to $parsedVolume***"
  }

  def reset(event: MessageReceivedEvent): String = {
    kachMng.scheduler.queue.clear()
    event.getGuild.getAudioManager.setSendingHandler(kachMng.sendHandler)
    "***Drop all tracks to the toilet***"
  }

  def shuffle(): String = {
    val shuffled = Random.shuffle(kachMng.scheduler.queue)
    kachMng.scheduler.queue.clear()
    kachMng.scheduler.queue.enqueueAll(shuffled)
    "***Shuffling queue!***"
  }

  def np(): String = {
    s"***Now playing ${kachMng.audioPlayer.getPlayingTrack.getInfo.title}***"
  }

  def down(event: MessageReceivedEvent): String = {
    reset(event)
    skip()
    "***Shutting down***"
  }

  private def pickVoiceChannel(event: MessageReceivedEvent): VoiceChannel = {
    val guild = event.getGuild
    val channels = guild.getVoiceChannels.asScala.toList

    channels.find(vc => !vc.getMembers.isEmpty) match {
      case Some(value) => value
      case None => channels.head
    }
  }


  class KachLoadResultHandler extends AudioLoadResultHandler {
    override def trackLoaded(track: AudioTrack): Unit = kachMng.scheduler.queueTrack(track)

    override def playlistLoaded(playlist: AudioPlaylist): Unit = {}

    override def noMatches(): Unit = {}

    override def loadFailed(exception: FriendlyException): Unit = {}
  }
}
