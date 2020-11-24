package by.gstu.beans.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    private static final Logger logger = LogManager.getLogger();

    private ObjectOutputStream outMessage; //исходящие сообщения
    private ObjectInputStream inMessage; //входящие message
    private Socket clientSocket; //клиентский сокет
    private Server server; //сервер

    public ClientHandler(Socket clientSocket, Server server){
        try{
            this.server = server;
            this.clientSocket = clientSocket;
            outMessage = new ObjectOutputStream(clientSocket.getOutputStream());
            inMessage = new ObjectInputStream(clientSocket.getInputStream());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getClientSocket() {return clientSocket;}

    public void setClientSocket(Socket clientSocket){
        try{
            this.clientSocket = clientSocket;
            logger.info("Client socket successfully created");
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    @Override
    public void run() {
        try{
            server.sendMessageToAllClients("Новый участник вошел в чат!");
            while(!clientSocket.isClosed()){
                String clientMessage = (String) inMessage.readObject();
                if (clientMessage.equalsIgnoreCase("exit")) {
                    closeConnection();
                    server.removeClient(this);
                    break;
                }
                server.sendMessageToAllClients(clientMessage);
            }
            clientSocket.close();
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    public void closeConnection(){
        String msg = "Клиент " + clientSocket.getInetAddress() + "отключился от чата";
        server.sendMessageToAllClients(msg);
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
