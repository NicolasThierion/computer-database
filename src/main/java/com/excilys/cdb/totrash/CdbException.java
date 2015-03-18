package com.excilys.cdb.totrash;

/**
 * 
 * @author Nicolas THIERION
 * @version 0.1.0
 *
 */
public class CdbException extends Exception {
	
	private static final long serialVersionUID = 2889006432165360536L;
	
	private String mMessage;
	
	public CdbException(String what) {
		mMessage = what;
	}
	
	public String getMessage()  {
		return mMessage;
	}
	
}
