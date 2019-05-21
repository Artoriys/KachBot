package dbLogAddon;

import com.mysql.cj.jdbc.MysqlDataSource;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Log4j(topic = "file")
public class DataSourceConfiguration {

    private static final MysqlDataSource MYSQL_DATA_SOURCE = new MysqlDataSource();
    private static final List<Connection> CONNECTIONS = new ArrayList<>();

    static {
        Map<String, String> envVar = System.getenv();
        MYSQL_DATA_SOURCE.setURL(envVar.get("url"));
        MYSQL_DATA_SOURCE.setUser(envVar.get("username"));
        MYSQL_DATA_SOURCE.setPassword(envVar.get("password"));
    }

    private DataSourceConfiguration() {

    }

    private static void makeConnectionPool() {
        if (CONNECTIONS.size() > 2) return;
        while (CONNECTIONS.size() <= 5) {
            try {
                CONNECTIONS.add(MYSQL_DATA_SOURCE.getConnection());
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public static Connection getConnection() {
        makeConnectionPool();
        Connection connection = CONNECTIONS.get(0);
        CONNECTIONS.remove(0);
        return connection;
    }
}

