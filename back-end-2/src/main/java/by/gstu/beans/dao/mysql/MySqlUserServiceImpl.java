package by.gstu.beans.dao.mysql;

import by.gstu.beans.User;
import by.gstu.beans.dao.interfaces.UserService;
import by.gstu.beans.dao.sessionFactory.HibernateSessionFactoryUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.List;
import java.util.stream.Collectors;

public class MySqlUserServiceImpl implements UserService {

    private static Logger logger = LogManager.getLogger();

    @Override
    public boolean add(User user) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            return true;
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(User user) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            session.beginTransaction();
            session.delete(user);
            session.getTransaction().commit();
            return true;
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            User findUser = session.createQuery("from User ", User.class)
                    .stream()
                    .filter(o -> o.getId() == id)
                    .findAny().orElse(new User());
            session.delete(findUser);
            session.getTransaction().commit();
            return true;
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return false;
    }

    @Override
    public User getUser(int userId) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            return session.createQuery("from User ", User.class)
                    .stream()
                    .filter(o -> o.getId() == userId)
                    .findAny().orElse(new User());
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public List<User> getUsers() {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            return session.createQuery("from User ", User.class)
                    .stream()
                    .collect(Collectors.toList());
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public User checkUser(String nickname, String password) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            User user = session.createQuery("from User ", User.class)
                    .stream()
                    .filter(o -> o.getNickname().equals(nickname) && o.getPassword().equals(password))
                    .findAny().orElse(new User());
            return user;
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return null;
    }
}
