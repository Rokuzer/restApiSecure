package es.uca.ssd.restapisecure.exception;

@SuppressWarnings("serial")
public class DuplicateEmailException extends Exception {

    public DuplicateEmailException(String message) {
        super(message);
    }

}
