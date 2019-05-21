package dbLogAddon.handlers;

import dbLogAddon.DataSourceConfiguration;
import dbLogAddon.model.Message;
import dbLogAddon.repositories.LogRepository;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@Log4j(topic = "file")
public class LogRepositoryImpl implements LogRepository {

    @Override
    public void saveMassage(Message message) {
        Connection connection = DataSourceConfiguration.getConnection();
        String query = "insert into message_logs (date, user, channel_name, message) values (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(message.getLocalDateTime()));
            preparedStatement.setString(2, message.getUser());
            preparedStatement.setString(3, message.getChannel());
            preparedStatement.setString(4, message.getMessage());
            preparedStatement.execute();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }
}
