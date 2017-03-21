package by.sleipnirim.messageServer.bean;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by sleipnir on 16.3.17.
 */
@Entity
@Table(name = "Users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User implements Serializable{

    private Long id;
    private String login;
    private String password;
    private boolean online;
    private Set<User> friends;
    //private Set<User> friendsOf = new HashSet<User>();
    private Set<Message> messages;

    public User(){}

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsers")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "login")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "online")
    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Users_has_Users",
        joinColumns = @JoinColumn(name = "Users_idUsers"),
        inverseJoinColumns = @JoinColumn(name = "Users_idUsers1"))
    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    /*@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Users_has_Users",
            joinColumns = @JoinColumn(name = "Users_idUsers1"),
            inverseJoinColumns = @JoinColumn(name = "Users_idUsers"))
    public Set<User> getFriendsOf() {
        return friendsOf;
    }

    public void setFriendsOf(Set<User> friendsOf) {
        this.friendsOf = friendsOf;
    }*/

    @OneToMany(mappedBy = "to", cascade = CascadeType.ALL,
            orphanRemoval = true)
    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", online=" + online +
                ", friends=" + friends +
                '}';
    }
}
