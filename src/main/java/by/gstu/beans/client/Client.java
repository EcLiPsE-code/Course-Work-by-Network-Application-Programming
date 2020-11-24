package by.gstu.beans.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

import java.util.Objects;
import java.util.Scanner;

public class Client {
    private static final Logger logger = LogManager.getLogger();

    private static final String ADDRESS = "127.0.0.1"; //ip-адрес, по которому подключаемся к серверу
    private static final int PORT = 3443; //порт, по которому подключаемся

    private String address;
    private int port;

    public Client(String address, int port){
        this.address = Objects.requireNonNull(address);
        this.port = port;
    }

    public Client(int port){
        this.address = ADDRESS;
        this.port = port;
    }

    public Client(){
        this.address = ADDRESS;
        this.port = PORT;
    }

    public String getAddress(){return this.address;}
    public int getPort(){return this.port;}

    public void setAddress(String address){
        this.address = address;
    }
    public void setPort(int port){
        this.port = port;
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.connect();
    }

    public void connect(){
        try(Socket clientSocket = new Socket(address, port)){
            Scanner in = new Scanner(new InputStreamReader(System.in));
            logger.info("Client successfully connected at" + getAddress() + " with port " + getPort());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream()); //поток для входящих message
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream()); //поток для исходящих message

            while (!clientSocket.isOutputShutdown()) {
                System.out.println(inputStream.readObject());
                System.out.print("Enter message: ");
                String message = in.nextLine();
                if (message.equals("exit")){
                    outputStream.writeObject(message);
                    outputStream.flush();
                    clientSocket.close();
                    break;
                }
                outputStream.writeObject(message);
                outputStream.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
