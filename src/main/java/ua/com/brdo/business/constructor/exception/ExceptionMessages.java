package ua.com.brdo.business.constructor.exception;

import org.springframework.beans.factory.annotation.Autowired;
import ua.com.brdo.business.constructor.service.MessageByLocaleService;

public final class ExceptionMessages {

    @Autowired
    private static MessageByLocaleService messageByLocaleService;

    public static final String USER_ALREADY_GRANTED_THIS_ROLE = messageByLocaleService.getMessage("user.role.already_granted");
    public static final String USER_NOT_GRANTED_THIS_ROLE = messageByLocaleService.getMessage("user.role.not_granted");
    public static final String USER_CREATION_FAILURE = messageByLocaleService.getMessage("user.creation.failure");
    public static final String USER_UPDATE_FAILURE = messageByLocaleService.getMessage("user.update.failure");
    public static final String USER_DELETE_FAILURE = messageByLocaleService.getMessage("user.delete.failure");
    public static final String USER_FIND_FAILURE = messageByLocaleService.getMessage("user.find.failure");
    public static final String USER_NULL = messageByLocaleService.getMessage("user.validation.null");
    public static final String USER_NULL_USERNAME = messageByLocaleService.getMessage("user.validation.username.null");
    public static final String USER_NULL_EMAIL = messageByLocaleService.getMessage("user.validation.email.null");
    public static final String USER_EMPTY_EMAIL = messageByLocaleService.getMessage("user.validation.email.empty");
    public static final String USER_NULL_PASSWORD = messageByLocaleService.getMessage("user.validation.password.null");
    public static final String USER_EMPTY_PASSWORD = messageByLocaleService.getMessage("user.validation.password.empty");
    public static final String USER_NULL_PASSWORD_CONFIRM = messageByLocaleService.getMessage("user.validation.password.null_confirm");
    public static final String USER_EMPTY_PASSWORD_CONFIRM = messageByLocaleService.getMessage("user.validation.password.empty_confirm");
    public static final String USER_WRONG_EMAIL_FORMAT = messageByLocaleService.getMessage("user.validation.email.incorrect_format");
    public static final String USER_WRONG_PASSWORD_CHARS = messageByLocaleService.getMessage("user.validation.password.incorrect_chars");
    public static final String USER_WRONG_PASSWORD_LENGTH = messageByLocaleService.getMessage("user.validation.password.length");
    public static final String FIELD_NOT_UNIQUE = messageByLocaleService.getMessage("field.validation.not_unique");
    public static final String ROLE_CREATION_FAILURE = messageByLocaleService.getMessage("role.creation.failure");
    public static final String ROLE_UPDATE_FAILURE = messageByLocaleService.getMessage("role.update.failure");
    public static final String ROLE_DELETE_FAILURE = messageByLocaleService.getMessage("role.delete.failure");
    public static final String ROLE_FIND_FAILURE = messageByLocaleService.getMessage("role.find.failure");
    public static final String ROLE_NULL = messageByLocaleService.getMessage("role.validation.null");
    public static final String ROLE_NULL_TITLE = messageByLocaleService.getMessage("role.validation.title.null");

    private ExceptionMessages() {
    }
}
