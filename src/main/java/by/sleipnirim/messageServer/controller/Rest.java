package by.sleipnirim.messageServer.controller;

import by.sleipnirim.messageServer.bean.Message;
import by.sleipnirim.messageServer.bean.User;
import by.sleipnirim.messageServer.service.MessageService;
import by.sleipnirim.messageServer.service.UserService;
import by.sleipnirim.messageServer.socket.ServerSocketConnection;
import by.sleipnirim.messageServer.socket.ServerSocketConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created by sleipnir on 17.3.17.
 */

@RestController
public class Rest {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    ServerSocketConnection serverSocketConnection;

    //request like "/auth?id=login&password=pass"
    @RequestMapping(value = "auth", params = {"login", "password"}, method = RequestMethod.GET)
    ResponseEntity<User> login (@RequestParam("login") String login, @RequestParam("password") String password){
        User user = userService.findByLoginAndPassword(login, password);
        if (user == null) return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    @RequestMapping(value = "auth", params = {"login", "password"}, method = RequestMethod.POST)
    ResponseEntity<User> register (@RequestParam("login") String login, @RequestParam("password") String password){
        User user = new User(login, password);
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "getAll" ,method = RequestMethod.GET)
    ResponseEntity<Set<User>> getAll (){

        Set<User> users = userService.findAll();
        if (users == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "connect", method = RequestMethod.POST)
    ResponseEntity<HttpStatus> connect(@RequestBody User user) {
        try {
            serverSocketConnection.connect(user.getId());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ServerSocketConnectionException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "getMessages", method = RequestMethod.GET)
    ResponseEntity<Set<Message>> getMessages(@RequestBody User userFrom, User userTo) {
        Set<Message> messages = messageService.findByFromAndTo(userFrom, userTo);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }


    @Autowired
    public void setServerSocketConnection(ServerSocketConnection serverSocketConnection) {
        this.serverSocketConnection = serverSocketConnection;
    }
}
