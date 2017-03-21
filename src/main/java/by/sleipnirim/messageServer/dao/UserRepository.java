package by.sleipnirim.messageServer.dao;

import by.sleipnirim.messageServer.bean.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by sleipnir on 17.3.17.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    User findByLogin (String login);
    User findByLoginAndPassword(String login, String password);




}
