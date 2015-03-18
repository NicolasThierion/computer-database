package com.excilys.cdb.model;

import java.io.Serializable;

/**
 * 
 * @author Nicolas THIERION
 * @version 0.2.0
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
	protected Long mId;
	
	/* ***
	 * CONSTRUCTORS
	 */
	
	public Company() {
		mName = DEFAULT_COMPANY_NAME;
		mId = -1L;
	}
	
	public Company(String name) {
		mName = name;
		mId = -1L;
	}
	
	public Company (long id, String name) {
		mName = name;
		mId = id;
	}
	
	public Company(Company manufacturer) {
		mName = manufacturer.mName;
		mId = manufacturer.mId;
	}
	/* ***
	 * ACCESSORS
	 */

	public void setId(Long id) {
		mId = id;
	}
	
	public Long getId() {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mId == null) ? 0 : mId.hashCode());
		result = prime * result + ((mName == null) ? 0 : mName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Company other = (Company) obj;
		if (mId == null) {
			if (other.mId != null)
				return false;
		} else if (!mId.equals(other.mId))
			return false;
		if (mName == null) {
			if (other.mName != null)
				return false;
		} else if (!mName.equals(other.mName))
			return false;
		return true;
	}
	
	
	

}
