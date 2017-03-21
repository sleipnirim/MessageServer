package by.sleipnirim.messageServer.controller;

import by.sleipnirim.messageServer.bean.User;
import by.sleipnirim.messageServer.service.UserService;
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

    //request like "/auth?id=login&password=pass"
    @RequestMapping(value = "auth", params = {"login", "password"}, method = RequestMethod.GET)
    ResponseEntity<User> login (@RequestParam("login") String login, @RequestParam("password") String password){
        User user = userService.findByLoginAndPassword(login, password);
        if(user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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


}
