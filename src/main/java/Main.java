import command.TextListener;
import lombok.extern.log4j.Log4j;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

@Log4j(topic = "file")
public class Main {
    private TextListener textListener = new TextListener();
    private JDABuilder builder;
    private static final String TOKEN = System.getenv("BOT_TOKEN");

    public static void main(String[] args) {
       new Main().startBot();
        log.info("Bot started");
    }

    private void botConfigure() {
        builder = new JDABuilder(AccountType.BOT);
        builder.setToken(TOKEN);
        builder.addEventListener(textListener);
        log.info("Bot configuration complete");
    }

    private void startBot() {
        botConfigure();
        try {
            builder.build();
        } catch (LoginException e) {
            log.error(e.getMessage(), e);
        }
    }


}
