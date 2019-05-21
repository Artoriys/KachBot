package dbLogAddon.repositories;

import dbLogAddon.model.Message;

public interface LogRepository {
    void saveMassage(Message message);
}
