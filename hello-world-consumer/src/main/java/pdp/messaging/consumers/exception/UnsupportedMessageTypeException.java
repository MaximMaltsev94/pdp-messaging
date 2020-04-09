package pdp.messaging.consumers.exception;

import javax.jms.Message;

public class UnsupportedMessageTypeException extends RuntimeException {
    public UnsupportedMessageTypeException() {
        super();
    }

    public UnsupportedMessageTypeException(Class<? extends Message> expected, Class<? extends Message> actual)  {
        this(expected, actual, null);
    }

    public UnsupportedMessageTypeException(Class<? extends Message> expected, Class<? extends Message> actual, Throwable cause) {
            this(String.format("Expected [%s], but found [%s]", expected.getName(), actual.getName()), cause);
    }

    public UnsupportedMessageTypeException(String description, Throwable cause) {
        super(description, cause);
    }
}
