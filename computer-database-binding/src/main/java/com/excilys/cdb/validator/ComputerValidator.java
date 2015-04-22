package com.excilys.cdb.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.model.Computer;


/**
 * Validates Computer & ComputerDto objects. Validation is done according to
 * defined rules :
 * <ul>
 * <li>Id = null || Id > 0</li>
 * <li>Release date & Discontinued date chronologically coherent.</li>
 * <li>Release date & Discontinued date have required format.</li>
 * </ul>
 *
 * @author Nicolas THIERION
 *
 */
@Component
public class ComputerValidator implements Validator {

    /* ***
     * ERROR CODE DEFINITIONS
     */
    private static enum ErrorCodes {
        NAME_IS_EMPTY("computer.error.name.empty"),
        DATE_CHRONOLOGY("computer.error.dates.chronology"),
        DATE_FORMAT("dates.error.format"),
        COMPUTER_ID("computer.id.negative");

        private final String mKey;
        ErrorCodes(String key) {
            mKey = key;
        }

        @Override
        public String toString() {
            return mKey;
        }
    }

    @Autowired
    private DateValidator mDateValidator;

    @Override
    public boolean supports(Class<?> clazz) {
        return ComputerDto.class.isAssignableFrom(clazz) || Computer.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        // check whether target is a Computer or a ComputerDto
        if (target instanceof ComputerDto) {
            mValidateComputerDto(((ComputerDto) target), errors);
        } else if (target instanceof Computer) {
            mValidateComputer((Computer) target, errors);
        }
    }

    private void mValidateComputer(Computer computer, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", ErrorCodes.NAME_IS_EMPTY.toString());
        if (computer.getId() != null && computer.getId() <= 0) {
            errors.rejectValue("id", ErrorCodes.COMPUTER_ID.toString());
        }

        if (computer.getReleaseDate() != null) {
            if (computer.getReleaseDate().compareTo(computer.getDiscontinuedDate()) > 0) {
                errors.rejectValue("introducedDate", ErrorCodes.DATE_CHRONOLOGY.toString());
                errors.rejectValue("discontinuedDate", ErrorCodes.DATE_CHRONOLOGY.toString());
            }
        }
    }

    private void mValidateComputerDto(ComputerDto computerDto, Errors errors) {

        // if computerDto, check date format validity.
        String releaseDateStr = computerDto.getIntroducedDate();
        String discontDateStr = computerDto.getDiscontinuedDate();

        // for security : nullify eventually invalid fields.
        if (computerDto.getCompanyId() != null && computerDto.getCompanyId() == 0) {
            computerDto.setCompanyDto(null);
        }
        if (releaseDateStr != null && releaseDateStr.trim().isEmpty()) {
            releaseDateStr = null;
        }
        if (discontDateStr != null && discontDateStr.trim().isEmpty()) {
            discontDateStr = null;
        }

        // check release date format
        if (!mDateValidator.isValid(releaseDateStr, true)) {
            errors.rejectValue("introducedDate", ErrorCodes.DATE_FORMAT.toString());
        }
        // check discont date format
        if (!mDateValidator.isValid(discontDateStr, true)) {
            errors.rejectValue("discontinuedDate", ErrorCodes.DATE_FORMAT.toString());
        }
        // check name
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", ErrorCodes.NAME_IS_EMPTY.toString());

        if (computerDto.getName() != null && !computerDto.getName().trim().isEmpty()) {
            mValidateComputer(computerDto.toComputer(), errors);
        }
    }
}
