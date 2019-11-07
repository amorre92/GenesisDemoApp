package amorre.genesis.demo.exception;

import java.text.MessageFormat;

/**
 * @author Anthony Morre
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, String arg1) {
        super(MessageFormat.format(message, arg1));
    }
}
