package com.excilys.cdb.model;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * @author Nicolas THIERION
 * @version 0.1.0
 *  
 *     TODO : The class is Serializable according to JavaBean standart
 *
 */
public class Computer implements Serializable{

	private static final long serialVersionUID = 3247508689250542994L;

	//TODO : name is mandatory. Remove default name?
	private static String DEFAULT_NAME = "Ordinateur sans nom";
	
	/* ***
	 * ATTRIBUTES
	 */
	
	/** name of this computer */
	private String mName;
	/** manufacturer of this computer */
	private Company mCompany;
	/** release date */
	private Date mReleaseDate;
	/** discontinuation date */
	private Date mEndingDate;
	/** id of this computer */
	private int mId = -1;;
		
	
	
	/* ***
	 * CONSTRUCTORS
	 */
	public Computer() {
		mName = DEFAULT_NAME;
		mCompany = null;
		mReleaseDate = mEndingDate = null;
	}
	
	public Computer(String name) {
		mName = name;
	}

	public Computer(int id, String name) {
		mName = name;
		mId  = id;
	}
	
	/* ***
	 * ACCESSORS
	 */
	public int getId() {
		return mId;
	}
	
	public void setId(int id) {
		mId = id;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		if(name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Computer name must not be empty");
		}
		mName = name;
	}

	public Company getCompany() {
		return mCompany;
	}

	public void setCompany(Company company) {
		mCompany = company;
	}

	public Date getReleaseDate() {
		return mReleaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		mReleaseDate = releaseDate;
	}

	public Date getEndingDate() {
		return mEndingDate;
	}

	public void setEndingDate(Date endingDate) {
		mEndingDate = endingDate;
	}
	
	
	/* ***
	 * OBJECT's OVERRIDES
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append("name=").append(mName).append(" ; manufacturer=").append(mCompany);
		sb.append(" : released=").append(mReleaseDate).append(" : discontinued=").append(mEndingDate);
		return sb.toString();
	}
	
	
	
	//	Show computer details (the detailed information of only one computer)
	

}
