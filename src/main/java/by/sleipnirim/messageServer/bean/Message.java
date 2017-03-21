package by.sleipnirim.messageServer.bean;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by sleipnir on 17.3.17.
 */
@Entity
@Table(name = "Messages")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Message {

    private Long id;
    private String message;
    private User from;
    private User to;
    private Date date;

    @Id
    @Column(name = "idMessages")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "Message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Users_idUsers_From")
    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Users_idUsers_To")
    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "Date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
