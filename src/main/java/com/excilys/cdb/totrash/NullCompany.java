package com.excilys.cdb.totrash;

import com.excilys.cdb.model.Company;

/**
 * Company with null id & null name.
 * @author Nicolas THIERION
 * @version 0.1.0
 */
public class NullCompany extends Company {

	private static final long serialVersionUID = -6120648916667342713L;

	public NullCompany() {
		super.mId = null;
		super.mName = null;
	}
}
