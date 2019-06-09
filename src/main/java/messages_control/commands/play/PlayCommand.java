package messages_control.commands.play;

import audio.AudioEventListener;
import audio.AudioPlayerHandler;
import audio.AudioResultHandlerImpl;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

public class PlayCommand {

    private MessageReceivedEvent event;

    public PlayCommand(MessageReceivedEvent event) {
        this.event = event;
    }

    public void play() {
        Guild guild = event.getGuild();
        VoiceChannel voiceChannel = guild.getVoiceChannelsByName("основной", true).get(0);
        AudioManager manager = guild.getAudioManager();
        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioPlayer audioPlayer = playerManager.createPlayer();

        AudioEventListener audioEventListener = new AudioEventListener();
        audioPlayer.addListener(audioEventListener);

        playerManager.loadItem("https://www.youtube.com/watch?v=MlwqOVnXuc4",
                new AudioResultHandlerImpl(audioPlayer));


        //YoutubeAudioSourceManager youtubeAudioSourceManager = new YoutubeAudioSourceManager();

        /*YoutubeAudioTrack audioTrack = new YoutubeAudioTrack(new AudioTrackInfo("U96 - Das Boot〔Techno-Version〕",
                "Speedyengel1", 500000 ,"MlwqOVnXuc4", false, "https://www.youtube.com/watch?v=MlwqOVnXuc4"),
                youtubeAudioSourceManager);*/
        //audioPlayer.playTrack(audioTrack);

        manager.setSendingHandler(new AudioPlayerHandler(audioPlayer));
        manager.openAudioConnection(voiceChannel);
    }
}
