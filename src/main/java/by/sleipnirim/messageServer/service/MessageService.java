package by.sleipnirim.messageServer.service;

import by.sleipnirim.messageServer.bean.Message;
import by.sleipnirim.messageServer.bean.User;

import java.util.Set;

/**
 * Created by sleipnir on 21.3.17.
 */
public interface MessageService {

    Message findById (Long id);
    Message save (Message message);

    Set<Message> findByFromAndTo(User from, User to);

}
