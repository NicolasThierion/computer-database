package com.excilys.cdb.model;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * @author Nicolas THIERION
 * @version 0.1.0
 *  
 * 
 *     TODO : The class is Serializable according to JavaBean standart
 *
 */
public class Computer implements Serializable{

	private static final long serialVersionUID = 3247508689250542994L;

	//TODO : name is mandatory. Remove default name?
	private static String DEFAULT_NAME = "Ordinateur sans nom";
	private static Integer DEFAULT_ID = null;

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
	private Date mDiscDate;
	/** id of this computer */
	private Integer mId = DEFAULT_ID;

	/* ***
	 * CONSTRUCTORS
	 */
	private void mNewComputer(Integer id, String name, Company manufacturer, Date releaseDate, Date discontinuedDate) {
		mName = name;
		mId  = id;
		mCompany = manufacturer;
		mReleaseDate = releaseDate;
		mDiscDate = discontinuedDate;
	}

	/**
	 * Create a new computer with a Null Company (NullCompany), null dates, default computer name & computer id=null
	 */
	public Computer() {
		mNewComputer(DEFAULT_ID, DEFAULT_NAME, null, null, null);
	}

	/**
	 * Create a new computer with given name, a Null Company (NullCOmpany), null dates & computer id=null
	 * @param name Name of this computer.
	 */
	public Computer(String name) {
		mNewComputer(DEFAULT_ID, name, null, null, null);
	}

	/**
	 * Create a new computer with given name, given computer ID, a Null Company (NullCOmpany) & null dates;
	 * @param id id of this computer.
	 * @param name Name of this computer.
	 * @throws IllegalArgumentException if parameter 'name' is null or empty, or if id provided is invalid.
	 */
	public Computer(int id, String name) throws IllegalArgumentException {
		if(id < 1)
			throw new IllegalArgumentException("Provided id=" + id + " is invalid");
		if(name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Computer name must not be empty");
		}
		
		mNewComputer(id, name, null, null, null);
	}
	
	/**
	 * Create a computer with given parameters.
	 * @param id id of this computer.
	 * @param name Name of this computer.
	 * @param manufacturer company that manufactured this computer.
	 * @param releaseDate when this computer has been released.
	 * @param discontinuedDate when this computer has been discontinued.
	 */
	public Computer(int id, String name, Company manufacturer, Date releaseDate, Date discontinuedDate) {
		mNewComputer(id, name, manufacturer, releaseDate, discontinuedDate);
	}

	/**
	 * Create a new computer with NULL computer id & given parameters.
	 * @param name Name of this computer.
	 * @param manufacturer Company that manufactured this computer.
	 * @param releaseDate when this computer has been released.
	 * @param discontinuedDate when this computer has been discontinued.
	 */
	public Computer(String name, Company manufacturer, Date releaseDate, Date discontinuedDate) {
		mNewComputer(DEFAULT_ID, name, manufacturer, releaseDate, discontinuedDate);
	}

	/* ***
	 * ACCESSORS
	 */
	/**
	 * May return null if this is a new computer, that has not yet been added to database.
	 * @return
	 */
	public Integer getId() {
		return mId;
	}

	public void setId(Integer id) {
		mId = id;
	}

	public String getName() {
		return mName;
	}

	/**
	 * 
	 * @param name
	 * @throws IllegalArgumentException if parameter 'name' is null or empty.
	 */
	public void setName(String name) throws IllegalArgumentException {
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

	public Date getIntroDate() {
		return mReleaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		mReleaseDate = releaseDate;
	}

	public Date getDiscontDate() {
		return mDiscDate;
	}

	public void setDiscontDate(Date discontDate) {
		mDiscDate = discontDate;
	}

	/* ***
	 * OBJECT's OVERRIDES
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append(getClass().getSimpleName()).append(" : ").append("id=").append(mId).append(" ; name=").append(mName);
		sb.append(" ; manufacturer=").append(mCompany);
		sb.append(" ; released=").append(mReleaseDate).append(" ; discontinued=").append(mDiscDate);
		sb.append("}");
		return sb.toString();
	}
}
