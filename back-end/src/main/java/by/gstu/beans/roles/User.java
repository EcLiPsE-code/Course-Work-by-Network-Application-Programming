package by.gstu.beans.roles;

import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class User extends Person{
    private String email;
    private String nickname;
    private String password;
    private Role role;

    public User(){}
    public User(int id, String fullName, String email, String nickname, String password, Role role){
        super(id, fullName);
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.role = Objects.requireNonNull(role);
    }

    public void setEmail(String email){
        this.email = Objects.requireNonNull(email);
    }
    public void setNickname(String nickname){
        this.nickname = Objects.requireNonNull(nickname);
    }
    public void setPassword(String password){
        this.password = Objects.requireNonNull(password);
    }
    public void setRole(Role role){
        this.role = Objects.requireNonNull(role);
    }

    public String getEmail(){return email;}
    public String getNickname(){return nickname;}
    public String getPassword(){return password;}
    public Role getRole(){return role;}

    @Override
    public String toString() {
        return super.toString() + "email: " + email + ", nickname: "
               + nickname + ", role" + role + "}";
    }
}
