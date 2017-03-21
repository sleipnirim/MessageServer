package by.sleipnirim.messageServer.service.impl;

import by.sleipnirim.messageServer.bean.User;
import by.sleipnirim.messageServer.dao.UserRepository;
import by.sleipnirim.messageServer.service.UserService;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Created by sleipnir on 17.3.17.
 */

@Service("userService")
@Repository
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return repository.findOne(id);
    }

    @Transactional(readOnly = true)
    public Set<User> findAll() {
        return Sets.newHashSet(repository.findAll());
    }

    public User save(User user) {
        return repository.save(user);
    }

    public void delete(User user) {
        repository.delete(user);
    }

    @Transactional(readOnly = true)
    public User findByLogin(String login) {
        return repository.findByLogin(login);
    }

    @Transactional(readOnly = true)
    public User findByLoginAndPassword(String login, String password) {
        return repository.findByLoginAndPassword(login, password);
    }
}
