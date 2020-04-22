package audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.{AudioTrack, AudioTrackEndReason}

class KachBotAudioEventAdapter extends AudioEventAdapter {
  override def onPlayerPause(player: AudioPlayer): Unit = player.setPaused(true)

  override def onPlayerResume(player: AudioPlayer): Unit = player.setPaused(false)
}
