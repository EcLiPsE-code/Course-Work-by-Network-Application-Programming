package by.gstu.beans;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "messages")
public class Message implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "Id")
    private int id;

    @Column(name = "Message")
    private String message;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "UserId")
    private User user;

    public Message(){}
    public Message(int id, String message, User user){
        this.id = id;
        this.message = Objects.requireNonNull(message);
        this.user = user;
    }

    public void setId(int id){
        this.id = id;
    }
    public void setMessage(String message){
        this.message = Objects.requireNonNull(message);
    }
    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){return user;}
    public int getId(){return id;}
    public String getMessage(){return message;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message1 = (Message) o;
        return getId() == message1.getId() &&
                Objects.equals(getMessage(), message1.getMessage()) &&
                Objects.equals(getUser(), message1.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getMessage(), getUser());
    }

    @Override
    public String toString() {
        return "{id: " + id + ", message: " + message + "}";
    }
}
