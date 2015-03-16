package com.excilys.cdb.model;

import java.io.Serializable;

/**
 * 
 * @author Nicolas THIERION
 * @version 0.1.0
 * TODO doc
 *
 */
public class Company implements Serializable{
	
	private static final long serialVersionUID = 1233212107449481466L;

	private static final String DEFAULT_COMPANY_NAME = "unknown company";
	
	/* ***
	 * ATTRIBUTES
	 */
	protected String mName;
	protected Integer mId;
	
	
	/* ***
	 * CONSTRUCTORS
	 */
	
	public Company() {
		mName = DEFAULT_COMPANY_NAME;
		mId = -1;
	}
	
	public Company(String name) {
		mName = name;
		mId = -1;
	}
	
	public Company (int id, String name) {
		mName = name;
		mId = id;
	}
	
	
	/* ***
	 * ACCESSORS
	 */
	
	public void setId(Integer id) {
		mId = id;
	}
	
	public Integer getId() {
		return mId;
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	public String getName() {
		return mName;
	}
	
	/* ***
	 * OBJECT OVERRIDES
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append(super.getClass().getSimpleName()).append(":").append(" : id=").append(mId).append(" : name=").append(mName);
		sb.append("}");
		return sb.toString();
	}
	
	
	

}
