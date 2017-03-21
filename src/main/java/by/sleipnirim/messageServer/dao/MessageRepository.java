package by.sleipnirim.messageServer.dao;

import by.sleipnirim.messageServer.bean.Message;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by sleipnir on 17.3.17.
 */
public interface MessageRepository extends CrudRepository<Message, Long> {

}
