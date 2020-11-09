package commands

import java.time.LocalTime

import audio.KachBotMusicManager
import com.sedmelluq.discord.lavaplayer.player.{AudioLoadResultHandler, AudioPlayer, DefaultAudioPlayerManager}
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.{AudioPlaylist, AudioTrack}
import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

import scala.jdk.CollectionConverters._
import scala.util.{Failure, Random, Success, Try}

class PlayCommand extends BotCommand {

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
    playerManager.loadItemOrdered(kachMng, url, new KachLoadResultHandler()).get()
    s"""***Track "$handleAddedTrack" added in queue""" + s"\n Now we have ${kachMng.scheduler.queue.length} tracks***"
  }

  def handleAddedTrack: String = {
    Try(kachMng.scheduler.queue.last) match {
      case Failure(_) => kachMng.scheduler.audioPlayer.getPlayingTrack.getInfo.title
      case Success(value) => value.getInfo.title
    }
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
    val queue = kachMng.scheduler.queue

    if (queue.isEmpty) {
      "***Queue is empty***"
    } else {
      np() +
        s"\n\n***Queue contains ${queue.length} tracks \n\n" +
        s"List of tracks:\n ${queue.map(_.getInfo.title).mkString("\n")}***"
    }
  }

  def skip(): String = {
    val playingTrackTitle = kachMng.scheduler.audioPlayer.getPlayingTrack.getInfo.title
    kachMng.scheduler.nextTrack()
    s"***Skipping $playingTrackTitle***"
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

  def np(): String =
    Option(kachMng.audioPlayer.getPlayingTrack) match {
      case Some(track) => s"***Now playing ${track.getInfo.title}***"
      case None => "***No track playing now!***"
    }

  def down(event: MessageReceivedEvent): String = {
    reset(event)
    skip()
    "***Shutting down***"
  }

  def pos(time: String): String = {
    Option(kachMng.audioPlayer.getPlayingTrack) match {
      case Some(track) => setTrackTime(time, track)
      case None => "***No track playing now!***"
    }
  }

  def tt(): String = {
    kachMng.audioPlayer.getPlayingTrack match {
      case track: AudioTrack => constructDurations(track.getDuration, track.getPosition)
      case _ => "***No track playing!***"
    }
  }

  def constructDurations(trackDuration: Long, currentPos: Long): String = {
    val trackDurSec = LocalTime.ofSecondOfDay(trackDuration / 1000)
    val currentPosSec = LocalTime.ofSecondOfDay(currentPos / 1000)

    s"***Current track position is: $currentPosSec\nTrack duration is: $trackDurSec***"
  }


  def setTrackTime(time: String, track: AudioTrack): String =
    Try(parseToMillis(time)) match {
      case Failure(_) => "***Something went wrong\nCheck your time***"
      case Success(millis) =>
        track.setPosition(millis)
        s"***Setting track time to $time***"
    }

  def parseToMillis(time: String): Long = {
    val times = time.split(":").map(_.toInt)

    assert(times.isInstanceOf[Array[Int]])

    matchingSeconds(times) * 1000
  }

  def matchingSeconds(times: Array[Int]): Int =
    times.length match {
      case 1 => LocalTime.of(0, 0, times(0)).toSecondOfDay
      case 2 => LocalTime.of(0, times(0), times(1)).toSecondOfDay
      case 3 => LocalTime.of(times(0), times(1), times(2)).toSecondOfDay
      case _ => throw new IllegalArgumentException
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
