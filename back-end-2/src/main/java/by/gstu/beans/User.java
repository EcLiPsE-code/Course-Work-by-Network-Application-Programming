package by.gstu.beans;

import by.gstu.beans.interfaces.Role;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends Person implements Serializable{
    @Column(name = "Email")
    private String email;

    @Column(name = "Nickname")
    private String nickname;

    @Column(name = "Role")
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Collection<Message> messages;

    @Column(name = "Password")
    private String password;
    
    public User(){}
    public User(int id, String fullName, String email, String nickname, Role role, String password){
        super(id, fullName);
        this.email = email;
        this.nickname = nickname;
        this.role = Objects.requireNonNull(role);
        this.password = Objects.requireNonNull(password);
    }
    public User(String fullName, String email, String nickname, Role role, String password){
        super(fullName);
        this.email = email;
        this.nickname = nickname;
        this.role = Objects.requireNonNull(role);
        this.password = Objects.requireNonNull(password);
    }

    public void setEmail(String email){
        this.email = Objects.requireNonNull(email);
    }
    public void setNickname(String nickname){
        this.nickname = Objects.requireNonNull(nickname);
    }
    public void setRole(Role role){
        this.role = Objects.requireNonNull(role);
    }
    public void setMessages(Set<Message> messages){
        this.messages = messages;
    }

    public String getEmail(){return email;}
    public String getNickname(){return nickname;}
    public Role getRole(){return role;}
    public String getPassword(){return password;}
    public Collection<Message> getMessages(){return messages;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getNickname(), user.getNickname()) &&
                getRole() == user.getRole() &&
                Objects.equals(getMessages(), user.getMessages()) &&
                Objects.equals(getPassword(), user.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getEmail(), getNickname(), getRole(), getMessages(), getPassword());
    }

    @Override
    public String toString() {
        return super.toString() + ", email: " +email+ ", nickname: " +nickname+ ", role: " +role+ "}";
    }
}
