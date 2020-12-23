package by.gstu.beans.dao.interfaces;

import by.gstu.beans.User;

import java.util.List;

public interface UserService {
    boolean add(User user);
    boolean delete(User user);
    boolean delete(int id);
    User getUser(int userId);
    List<User> getUsers();
    User checkUser(String nickname, String password);
}
