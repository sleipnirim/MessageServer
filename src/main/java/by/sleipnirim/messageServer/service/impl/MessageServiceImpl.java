package by.sleipnirim.messageServer.service.impl;

import by.sleipnirim.messageServer.bean.Message;
import by.sleipnirim.messageServer.bean.User;
import by.sleipnirim.messageServer.dao.MessageRepository;
import by.sleipnirim.messageServer.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Created by sleipnir on 21.3.17.
 */
@Service("messageService")
@Repository
@Transactional
public class MessageServiceImpl implements MessageService{

    @Autowired
    MessageRepository repository;


    @Override
    @Transactional(readOnly = true)
    public Message findById(Long id) {
        return repository.findOne(id);
    }

    @Override
    public Message save(Message message) {
        return repository.save(message);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Message> findByFromAndTo(User from, User to) {
        return repository.findByFromAndAndTo(from, to);
    }
}
