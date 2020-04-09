package pdp.messaging.consumers.listeners;

import pdp.messaging.consumers.exception.UnsupportedMessageTypeException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class HelloWorldMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {

        if (! (message instanceof TextMessage)) {
            throw new UnsupportedMessageTypeException(TextMessage.class, message.getClass());
        }

        try {
            System.out.println("Message received: " + ((TextMessage) message).getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
