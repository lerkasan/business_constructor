package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import ua.com.brdo.business.constructor.service.MessageByLocaleService;

import java.util.Locale;

@Service
public final class MessageByLocaleServiceIpml implements MessageByLocaleService {

    @Autowired
    private MessageSource messageSource;

    @Override
    public String getMessage(String name) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(name,null,locale);
    }
}
