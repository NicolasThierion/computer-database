package com.excilys.cdb.dao;

/**
 * Thrown when something goes wrong with DAO layer.
 *
 * @author Nicolas THIERION
 * @version 0.1.0
 *
 * TODO doc
 */
public class DaoException extends RuntimeException {

    private static final long serialVersionUID = -8950985503551823818L;

    public enum ErrorType {
        UNKNOWN_ERROR(0), DAO_ERROR(1), SQL_ERROR(2), SYNTAX_ERROR(3), RESULT_ERROR(3);

        private int mCode;

        ErrorType(int code) {
            mCode = code;
        }

        public int getCode() {
            return mCode;
        }
    }

    private final String mWhat;

    public DaoException(String message, ErrorType sqlError) {
        mWhat = message;
    }

    @Override
    public String getMessage() {
        return mWhat;
    }
}
