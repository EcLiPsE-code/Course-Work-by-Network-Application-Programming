package by.gstu.beans.dao.interfaces;

import by.gstu.beans.Message;
import by.gstu.beans.User;

import java.util.List;

public interface MessageService {
    boolean add(Message message);
    boolean delete(Message mesage);
    boolean delete(int id);
    boolean update(Message message);
    List<Message> getMessages(User user);
    List<Message> getMessage(int userId);
}
