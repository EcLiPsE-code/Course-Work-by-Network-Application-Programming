package by.gstu.beans.server;

import by.gstu.beans.dao.interfaces.MessageService;
import by.gstu.beans.dao.interfaces.UserService;
import by.gstu.beans.dao.mysql.MySqlMessageServiceImpl;
import by.gstu.beans.dao.mysql.MySqlUserServiceImpl;
import by.gstu.beans.utils.json.MessageJsonConvert;
import by.gstu.beans.utils.json.UserJsonConvert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(value = "/chat")
public class WebSocketServer {
    private static final Logger logger = LogManager.getLogger();

    private final UserService userSerice = new MySqlUserServiceImpl();
    private final MessageService messageService = new MySqlMessageServiceImpl();

    private final MessageJsonConvert msgJSONConvert = new MessageJsonConvert();
    private final UserJsonConvert userJsonConvert = new UserJsonConvert();

    private static final Set<Session> clients = new HashSet<>();
    private static int nextId;

    public static void sendingMessageToClients(String message){
        synchronized (clients) {
            for (Session client : clients) {
                try {
                    client.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    clients.remove(client);
                }
            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            if (message != null){
                new Thread(() -> sendingMessageToClients(message)).start();
            }
        } catch (Exception e) {
            System.out.println("msg error");
        }
    }

    @OnOpen
    public void onOpen(Session session){
        //session.getUserProperties().put("username", "user_" + nextId++);
        //String username = session.getUserProperties().get("username").toString();
        //System.out.println(username + " connected to server");
        //sendingMessageToClients(username + " connected to server");
        synchronized (clients) {
            clients.add(session);
        }
    }

    @OnClose
    public void onClose(Session session) {
        synchronized (session) {
            clients.remove(session);
        }
        System.out.println("client disconnected: " + session.getUserProperties().get("username"));
    }

    @OnError
    public void onError(Session session, Throwable throwable ) {
        System.out.println("Error socket, maybe someone disconnected.");
    }
}
