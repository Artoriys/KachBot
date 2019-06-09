package audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class AudioResultHandlerImpl implements AudioLoadResultHandler {

    private AudioPlayer audioPlayer;

    public AudioResultHandlerImpl(AudioPlayer player) {
        this.audioPlayer = player;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        audioPlayer.playTrack(track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {

    }

    @Override
    public void noMatches() {

    }

    @Override
    public void loadFailed(FriendlyException exception) {

    }
}
