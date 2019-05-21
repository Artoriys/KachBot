package dbLogAddon;

import lombok.extern.log4j.Log4j;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;


@Log4j(topic = "file")
public class DataSourceConfiguration {

    private static final BasicDataSource MYSQL_DATA_SOURCE = new BasicDataSource();

    static {
        Map<String, String> envVar = System.getenv();
        MYSQL_DATA_SOURCE.setUrl(envVar.get("url"));
        MYSQL_DATA_SOURCE.setUsername(envVar.get("username"));
        MYSQL_DATA_SOURCE.setPassword(envVar.get("password"));
        MYSQL_DATA_SOURCE.setMinIdle(2);
        MYSQL_DATA_SOURCE.setMaxIdle(5);
        MYSQL_DATA_SOURCE.setMaxOpenPreparedStatements(10);
        MYSQL_DATA_SOURCE.setValidationQuery("select id from message_logs");
        MYSQL_DATA_SOURCE.setTestWhileIdle(true);
        MYSQL_DATA_SOURCE.setTestOnBorrow(true);
    }

    private DataSourceConfiguration() {

    }

    public static Connection getConnection() throws SQLException {
        return MYSQL_DATA_SOURCE.getConnection();
    }
}

