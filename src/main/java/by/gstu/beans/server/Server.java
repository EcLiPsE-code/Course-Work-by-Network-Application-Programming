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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Server {
    private static final Logger logger = LogManager.getLogger();

    private static final int PORT = 3443;
    private final ExecutorService executorService = Executors.newCachedThreadPool(); //пулл потоков

    private ObjectInputStream mainIn; //для принятия сообщений от клиентов
    private ObjectOutputStream mainOut;

    private ServerSocket serverSocket;
    private int port;

    private List<ClientHandler> clients; //список подключенных клиентов

    public Server(){
        try {
            serverSocket = new ServerSocket(PORT);
            clients = new ArrayList<>();
            logger.info("Server socket successfully created");
        } catch (IOException e) {
            logger.error("Error created server socket");
        }
    }

    public Server(int port){
        try {
            setPort(port);
            serverSocket = new ServerSocket(port);
            clients = new ArrayList<>();
            logger.info("Server socket successfully created");
        } catch (IOException e) {
            logger.error("Error created server socket");
        }
    }

    public int getPort(){return port;}
    public void setPort(int port){
        this.port = port;
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

    public void run(){
        try{
            System.out.println("Server successfully started");
            logger.info("Server successfully started");
            System.out.println("Wait connection clients...");
            logger.info("Wait connection clients...");
            System.out.println("Count clients in the chat: " + clients.size());
            while(!serverSocket.isClosed()){
                final Socket clientSocket = serverSocket.accept();
                System.out.println("New client successfully connected " + clientSocket.getInetAddress() + " by port "
                        + clientSocket.getPort());
                logger.info("New client succesfully connected " + clientSocket.getInetAddress() + " by port " +
                        clientSocket.getPort());
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                System.out.println("Count clients in the chat: " + clients.size());
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
        System.out.println("User" + clientHandler.getClientSocket().getInetAddress() + " disconnected");
        System.out.println("Count clients in the chat: " + clients.size());
    }
}
