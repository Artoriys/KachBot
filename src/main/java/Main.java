import command.TextListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

public class Main {
    private TextListener textListener = new TextListener();
    private JDABuilder builder;
    private static final String TOKEN = "NTU2Nzc5Mjc0MzQzMDIyNTk0.D2-tmg.B9vdeTk7r8kzbiQpAkc4edakMMo";

    public static void main(String[] args) {
       new Main().startBot();

    }

    private void botConfigure() {
        builder = new JDABuilder(AccountType.BOT);
        builder.setToken(TOKEN);
        builder.addEventListener(textListener);
    }

    private void startBot() {
        botConfigure();
        try {
            builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }


}
