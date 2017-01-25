package ua.com.brdo.business.constructor.constraint;

public interface UniqueValidatable {

    String IS_AVAILABLE = "isAvailable";

    boolean isAvailable(String field, String value, Long id);
}
