package by.gstu.beans.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import by.gstu.beans.utils.ParserProperties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class Server {
    private static final Logger logger = LogManager.getLogger();

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private ObjectInputStream inMessage;
    private ObjectOutputStream outMessage;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients; //список подключенных клиентов

    public Server(){
        try {
            ParserProperties properties = new ParserProperties();
            serverSocket = new ServerSocket(Integer.parseInt(properties.getProperty("port")));
            clients = new ArrayList<>();
            logger.info("Server socket successfully created");
        } catch (IOException e) {
            logger.error("Error created server socket");
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

    public void run(){
        try{
            System.out.println("Server run");
            while (!serverSocket.isClosed()) {
                logger.info("Wait new client...");
                final Socket clientSocket = serverSocket.accept();
                logger.info("New client connected " + clientSocket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                executorService.execute(clientHandler);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    //метод для отправки сообщения всех user
    public void sendMessageToAllClients(String message){
        for (ClientHandler clientHandler : clients){
            clientHandler.sendMsg(message);
        }
    }

    //удаление клиента из чата
    public void removeClient(ClientHandler clientHandler){
        clients.remove(clientHandler);
        logger.info("User disconnected");
        logger.info("Count clients in the chat: " + clients.size());
    }
}
