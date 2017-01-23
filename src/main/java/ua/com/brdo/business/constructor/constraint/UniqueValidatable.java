package ua.com.brdo.business.constructor.constraint;

public interface UniqueValidatable {

    boolean isAvailable(String field, String value, Long id);
}
