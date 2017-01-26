package ua.com.brdo.business.constructor.constraint;

public interface UniqueValidatable {

    boolean isAvailable(String fieldName, String fieldValue, Long id);
}
