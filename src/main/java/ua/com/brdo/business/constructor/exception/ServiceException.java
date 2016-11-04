package ua.com.brdo.business.constructor.exception;

public class ServiceException extends Exception {

    public ServiceException() {
        super();
    }

    public ServiceException(String s) {
        super(s);
    }

    public ServiceException(String s, Exception e) {
        super(s,e);
    }
}
