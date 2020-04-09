package pdp.messaging.producers.helloworldproducer;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class HelloWorldProducer {

    private static Scanner console = new Scanner(System.in);
    private static final Set<String> EXIT_COMMANDS = new HashSet<>(Arrays.asList("q", "quit", "exit"));

    private static Context jndiCtx;
    private static ConnectionFactory connectionFactory;
    private static Destination destination;

    private static String DESTINATION_NAME = "helloqueue";


    public static void main(String[] args) throws NamingException {
        initMessagingBeans();

        for(;;) {
            System.out.print("Please enter message for sending to broker (q for exit): ");
            String messageToSend = console.nextLine();
            if(EXIT_COMMANDS.contains(messageToSend)) {
                break;
            }
            sendMessage(messageToSend);
        }

        freeResources();

    }

    private static void initMessagingBeans() throws NamingException {
        jndiCtx = new InitialContext();
        connectionFactory = (ConnectionFactory) jndiCtx.lookup("ConnectionFactory");
        destination = (Destination) jndiCtx.lookup(DESTINATION_NAME);
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

    private static void sendMessage(String message) {

        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destination);
            TextMessage textMessage = session.createTextMessage(message);
            producer.send(textMessage);
        } catch (JMSException e) {
            if(connection != null) {
                try {
                    // From JavaEE documentation:
                    // Closing a connection also closes its sessions and their message producers and message consumers.
                    connection.close();
                } catch (JMSException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


}
