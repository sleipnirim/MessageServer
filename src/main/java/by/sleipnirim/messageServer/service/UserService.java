package by.sleipnirim.messageServer.service;

import by.sleipnirim.messageServer.bean.User;

import java.util.Set;

/**
 * Created by sleipnir on 17.3.17.
 */
public interface UserService {

    User findById (Long id);
    Set<User> findAll ();
    User save (User user);
    void delete (User user);
    User findByLogin (String login);
    User findByLoginAndPassword(String login, String password);


}
