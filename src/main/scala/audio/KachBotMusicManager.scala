package audio

import com.sedmelluq.discord.lavaplayer.player.{AudioPlayer, AudioPlayerManager}

class KachBotMusicManager(manager: AudioPlayerManager) {

  val audioPlayer: AudioPlayer = manager.createPlayer()
  val scheduler = new TrackScheduler(audioPlayer)
  val sendHandler = new KachBotAudioSendHandler(audioPlayer)

  audioPlayer.addListener(scheduler)
}
