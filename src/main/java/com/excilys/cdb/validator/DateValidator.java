package com.excilys.cdb.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class DateValidator implements ConstraintValidator<DateFormat, String> {
    private static final String DATE_FORMAT_CODE = "date.format";

    @Autowired
    private MessageSource       messageSource;
    private String              mDatePattern;
    private DateFormat          mConstraintAnnotation;



    @Override
    public void initialize(DateFormat constraintAnnotation) {
        final Locale locale = LocaleContextHolder.getLocale();
        mConstraintAnnotation = constraintAnnotation;
        mDatePattern = messageSource.getMessage(DATE_FORMAT_CODE, null, locale);

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return isValid(value, mConstraintAnnotation.acceptEmpty());
    }

    public boolean isValid(String value, boolean acceptEmpty) {

        if (mDatePattern == null) {
            initialize(null);
        }

        if (acceptEmpty && (value == null || value.trim().isEmpty())) {
            return true;
        }
        if (GenericValidator.isDate(value, mDatePattern, true)) {
            try {
                final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(mDatePattern);
                LocalDate.parse(value, dateFormatter);
                return true;
            } catch (final DateTimeParseException e) {
                return false;
            }
        } else {
            return false;
        }
    }
}
