package es.uca.ssd.restapisecure.exception;

@SuppressWarnings("serial")
public class DuplicateUsernameException extends Exception {

    public DuplicateUsernameException(String message) {
        super(message);
    }

}
