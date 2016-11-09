package ua.com.brdo.business.constructor.exception;

public class ControllerException extends RuntimeException {

    public ControllerException() {
        super();
    }

    public ControllerException(String s) {
        super(s);
    }

    public ControllerException(String s, Exception e) {
        super(s, e);
    }
}
