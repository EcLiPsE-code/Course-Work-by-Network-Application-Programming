package by.gstu.beans.dao.mysql;

import by.gstu.beans.Message;
import by.gstu.beans.User;
import by.gstu.beans.dao.interfaces.MessageService;
import by.gstu.beans.dao.sessionFactory.HibernateSessionFactoryUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import java.util.List;
import java.util.stream.Collectors;

public class MySqlMessageServiceImpl implements MessageService {

    private static Logger logger = LogManager.getLogger();

    @Override
    public boolean add(Message message) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            session.beginTransaction();
            session.save(message);
            session.getTransaction().commit();
            return true;
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Message mesage) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            session.beginTransaction();
            session.delete(mesage);
            session.getTransaction().commit();
            return true;
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            session.beginTransaction();
            Message message = session.get(Message.class, id);
            session.delete(message);
            session.getTransaction().commit();
            return true;
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Message message) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            session.beginTransaction();
            session.update(message);
            session.getTransaction().commit();
            return true;
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
            return false;
        }
    }

    @Override
    public List<Message> getMessages(User user) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            return user.getMessages()
                    .stream()
                    .filter(o -> o.getId() == user.getId())
                    .collect(Collectors.toList());

        }
        catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public List<Message> getMessage(int userId) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            return session.createQuery("from Message", Message.class)
                    .list()
                    .stream()
                    .filter(o -> o.getId() == userId)
                    .collect(Collectors.toList());

        }
        catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return null;
    }
}
