package pdp.messaging.producers.helloworldproducer;

import javax.jms.*;
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
    private static MessageProducer producer;

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

    }

    private static void initMessagingBeans() throws NamingException {
        jndiCtx = new InitialContext();
        connectionFactory = (ConnectionFactory) jndiCtx.lookup("ConnectionFactory");
        destination = (Destination) jndiCtx.lookup(DESTINATION_NAME);
    }

    private static void sendMessage(String message) {

        Connection connection = null;
        Session session = null;
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer(destination);
            TextMessage textMessage = session.createTextMessage(message);
            producer.send(textMessage);
        } catch (JMSException e) {
            if(connection != null) {
                try {
                    connection.close();
                } catch (JMSException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


}
