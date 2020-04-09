package pdp.messaging.consumers.helloworldconsumer;

import pdp.messaging.consumers.listeners.HelloWorldMessageListener;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class HelloWorldConsumer {

    private static Context jndiCtx;
    private static ConnectionFactory connectionFactory;
    private static Destination destination;
    private static Connection connection = null;
    private static MessageListener messageListener = new HelloWorldMessageListener();

    private static String DESTINATION_NAME = "helloqueue";

    public static void main(String[] args) throws NamingException {
        initMessagingBeans();
        createListener();
        handleUserExitCommand();
        freeResources();
    }


    private static void initMessagingBeans() throws NamingException {
        jndiCtx = new InitialContext();
        connectionFactory = (ConnectionFactory) jndiCtx.lookup("ConnectionFactory");
        destination = (Destination) jndiCtx.lookup(DESTINATION_NAME);
    }

    private static void createListener() {
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(messageListener);
            connection.start(); //vital importance
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private static void freeResources() {
        if(jndiCtx != null) {
            try {
                jndiCtx.close();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
    }

    private static void handleUserExitCommand() {
        Scanner console = new Scanner(System.in);
        System.out.print("Start listening for incoming messages, type quit for exit: ");
        while (!"quit".equals(console.nextLine())){

        }
    }
}
