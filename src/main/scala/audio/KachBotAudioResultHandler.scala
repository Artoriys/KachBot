package audio

import com.sedmelluq.discord.lavaplayer.player.{AudioLoadResultHandler, AudioPlayer}
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.{AudioPlaylist, AudioTrack}

class KachBotAudioResultHandler(audioPlayer: AudioPlayer) extends AudioLoadResultHandler {
  override def trackLoaded(track: AudioTrack): Unit = {
    audioPlayer.playTrack(track)
  }

  override def playlistLoaded(playlist: AudioPlaylist): Unit = {}

  override def noMatches(): Unit = {}

  override def loadFailed(exception: FriendlyException): Unit = {}
}
