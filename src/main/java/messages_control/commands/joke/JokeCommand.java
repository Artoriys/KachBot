package messages_control.commands.joke;

import lombok.extern.log4j.Log4j;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Log4j(topic = "file")
public class JokeCommand {

    private String url = "https://official-joke-api.appspot.com/random_joke";
    private JSONObject jsonObject;

    public JokeCommand() {
        try {
            String json = new Scanner(new URL(url).openStream(), StandardCharsets.UTF_8).useDelimiter("\\A").next();
            jsonObject = new JSONObject(json);
        } catch (IOException e) {
            log.error(e);
        }
    }

    public String getJoke() {
        return jsonObject.getString("setup") +
                "\n" +
                jsonObject.getString("punchline");
    }
}
