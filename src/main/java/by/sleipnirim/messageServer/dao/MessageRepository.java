package by.sleipnirim.messageServer.dao;

import by.sleipnirim.messageServer.bean.Message;
import by.sleipnirim.messageServer.bean.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

/**
 * Created by sleipnir on 17.3.17.
 */
public interface MessageRepository extends CrudRepository<Message, Long> {

    Set<Message> findByFromAndAndTo(User from, User to);

}
