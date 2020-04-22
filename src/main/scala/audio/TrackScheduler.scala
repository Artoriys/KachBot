package audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.{AudioTrack, AudioTrackEndReason}

import scala.collection.mutable
import scala.util.{Failure, Success, Try}

class TrackScheduler(player: AudioPlayer) extends AudioEventAdapter {

  val queue: mutable.Queue[AudioTrack] = mutable.Queue()
  val audioPlayer: AudioPlayer = player

  def queueTrack(audioTrack: AudioTrack): Unit = {
    if (!audioPlayer.startTrack(audioTrack, true)){
      queue.enqueue(audioTrack)
    }
  }

  def nextTrack(): Unit = {
    val tryDequeueTrack = Try(queue.dequeue())
    tryDequeueTrack match {
      case Failure(_) => audioPlayer.stopTrack()
      case Success(value) => audioPlayer.startTrack(value, false)
    }
  }

  override def onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason): Unit = {
    if (endReason.mayStartNext) {
      nextTrack()
    }
  }
}
