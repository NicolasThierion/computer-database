package com.excilys.cdb.service;

public class ServiceException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 6857022515085054997L;

    private final String            mError;

    public ServiceException(String what) {
        mError = what;
    }

    @Override
    public String getMessage() {
        return mError;
    }

}
