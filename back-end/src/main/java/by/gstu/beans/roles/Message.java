package by.gstu.beans.roles;

import java.util.Objects;

public class Message {
    private int id;
    private String message;
    private int userId;

    public Message(){}
    public Message(int id, String message, int userId){
        this.id = id;
        this.message = Objects.requireNonNull(message);
        this.userId = userId;
    }

    public void setId(int id){
        this.id = id;
    }
    public void setMessage(String message){
        this.message = Objects.requireNonNull(message);
    }
    public void setUserId(int userId){
        this.userId = userId;
    }

    public int getId(){return id;}
    public String getMessage(){return message;}
    public int getUserId(){return userId;}
    

    @Override
    public String toString() {
        return "{id: " + id + ", message: " + message + ", role: " + role + "}";
    }
}
