package by.gstu.beans.client;

import by.gstu.beans.utils.ParserProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

public class Client extends JFrame{
    private static final Logger logger = LogManager.getLogger();

    private JTextField jtfMessage;
    private JTextField jtfName;
    private JTextArea jtaTextAreaMessage;
    private JLabel jlNumberOfClients;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Socket clientSocket;

    private String clientName = "";
    public String getClientName() {
        return clientName;
    }

    Client(){
        try{
            ParserProperties properties = new ParserProperties();
            clientSocket = new Socket(properties.getProperty("host"), Integer.parseInt(properties.getProperty("port")));
            inputStream = new ObjectInputStream(clientSocket.getInputStream()); //поток для входящих message
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream()); //поток для исходящих message
        } catch (IOException e) {
            e.printStackTrace();
        }
        createUI();
        setVisible(true);
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.connect();
    }

    public void connect(){
        try{
            while(!clientSocket.isOutputShutdown()) {
                // если есть входящее сообщение
                // считываем его
                String inMes = (String) inputStream.readObject();
                String clientsInChat = "Клиентов в чате = ";
                if (inMes.indexOf(clientsInChat) == 0) {
                    jlNumberOfClients.setText(inMes);
                }
                else {
                    // выводим сообщение
                    jtaTextAreaMessage.append(inMes);
                    // добавляем строку перехода
                    jtaTextAreaMessage.append("\n");
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    public void createUI(){
        setBounds(600, 300, 600, 500);
        setTitle("Client");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jtaTextAreaMessage = new JTextArea();
        jtaTextAreaMessage.setEditable(false);
        jtaTextAreaMessage.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(jtaTextAreaMessage);
        add(jsp, BorderLayout.CENTER);
        // label, который будет отражать количество клиентов в чате
        jlNumberOfClients = new JLabel("Количество клиентов в чате: ");
        add(jlNumberOfClients, BorderLayout.NORTH);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        add(bottomPanel, BorderLayout.SOUTH);
        JButton jbSendMessage = new JButton("Отправить");
        bottomPanel.add(jbSendMessage, BorderLayout.EAST);
        jtfMessage = new JTextField("Введите ваше сообщение: ");
        bottomPanel.add(jtfMessage, BorderLayout.CENTER);
        jtfName = new JTextField("Введите ваше имя: ");
        bottomPanel.add(jtfName, BorderLayout.WEST);
        // при фокусе поле сообщения очищается
        jtfMessage.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtfMessage.setText("");
            }
        });
        // при фокусе поле имя очищается
        jtfName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtfName.setText("");
            }
        });
        onclickSendMsg(jbSendMessage);
        onclickCloseWindow();
    }

    private void onclickSendMsg(JButton jbSendMessage){
        // обработчик события нажатия кнопки отправки сообщения
        jbSendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // если имя клиента, и сообщение непустые, то отправляем сообщение
                if (!jtfMessage.getText().trim().isEmpty() && !jtfName.getText().trim().isEmpty()) {
                    clientName = jtfName.getText();
                    sendMsg();
                    // фокус на текстовое поле с сообщением
                    jtfMessage.grabFocus();
                }
            }
        });
    }

    private void onclickCloseWindow(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    // здесь проверяем, что имя клиента непустое и не равно значению по умолчанию
                    if(!clientName.isEmpty() && !clientName.equals("Введите ваше имя: ")) {
                        outputStream.writeObject(clientName + " вышел из чата!");
                    }
                    else {
                        outputStream.writeObject("Участник вышел из чата, так и не представившись!");
                    }
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                    clientSocket.close();
                } catch (IOException exc) {
                    logger.error(exc.getMessage());
                }
            }
        });
    }

    public void sendMsg() {
        try{
            // формируем сообщение для отправки на сервер
            String messageStr = jtfName.getText() + ": " + jtfMessage.getText();
            // отправляем сообщение
            outputStream.writeObject(messageStr);
            outputStream.flush();
            jtfMessage.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
