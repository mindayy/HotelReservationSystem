package util.exception;

/**
 *
 * @author kaixin
 */
public class InvalidLoginCredentialException extends Exception {
    public InvalidLoginCredentialException() {
    }

    public InvalidLoginCredentialException(String msg) {
        super(msg);
    }
    
}
