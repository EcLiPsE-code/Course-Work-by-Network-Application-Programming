package by.gstu.beans.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private static final Logger logger = LogManager.getLogger();
    private static int clientCount = 0;

    private ObjectOutputStream outMessage; //исходящие сообщения
    private ObjectInputStream inMessage; //входящие message
    private Socket clientSocket; //клиентский сокет
    private Server server; //сервер

    public ClientHandler(Socket clientSocket, Server server){
        try{
            clientCount++;
            this.server = server;
            this.clientSocket = clientSocket;
            outMessage = new ObjectOutputStream(clientSocket.getOutputStream());
            inMessage = new ObjectInputStream(clientSocket.getInputStream());
        }catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void run() {
        try{
            if (clientSocket.isClosed()) throw new IllegalStateException();

            server.sendMessageToAllClients("Новый участник вошел в чат!");
            server.sendMessageToAllClients("Клиентов в чате = " + clientCount);
            while(!clientSocket.isClosed()){
                String message = (String) inMessage.readObject();
                server.sendMessageToAllClients(message);
            }
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
        finally {
            this.closeConnection();
        }
    }

    public void closeConnection(){
        server.removeClient(this);
        clientCount--;
        server.sendMessageToAllClients("Клиентов в чате = " + clientCount);
    }

    public void sendMsg(String msg){
        try{
            outMessage.writeObject(msg);
            outMessage.flush();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }
}
