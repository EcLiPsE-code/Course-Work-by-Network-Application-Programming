package by.gstu.beans.roles;

import java.util.Objects;

public class Message {
    private int id;
    private String message;
    private Role role;

    public Message(){}
    public Message(int id, String message, Role role){
        this.id = id;
        this.message = Objects.requireNonNull(message);
        this.role = Objects.requireNonNull(role);
    }

    public void setId(int id){
        this.id = id;
    }
    public void setMessage(String message){
        this.message = Objects.requireNonNull(message);
    }
    public void setRole(Role role){
        this.role = Objects.requireNonNull(role);
    }

    public int getId(){return id;}
    public String getMessage(){return message;}
    public Role getRole(){return role;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message1 = (Message) o;
        return getId() == message1.getId() &&
                Objects.equals(getMessage(), message1.getMessage()) &&
                getRole() == message1.getRole();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getMessage(), getRole());
    }

    @Override
    public String toString() {
        return "{id: " + id + ", message: " + message + ", role: " + role + "}";
    }
}
