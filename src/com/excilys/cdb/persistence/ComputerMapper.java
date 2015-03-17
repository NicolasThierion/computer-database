package com.excilys.cdb.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

public class ComputerMapper {

	/** name of this computer */
	public String name;
	/** manufacturer of this computer */
	public Company company;
	/** release date */
	public Date releaseDate;
	/** discontinuation date */
	public Date discDate;
	/** id of this computer. Remember to test if not null!!!! */
	public Long id;

	public java.sql.Timestamp sqlReleaseDate;
	public java.sql.Timestamp sqlDiscDate;
	/** remember to test if not null!!!! */
	public Long companyId;
	public String companyName;


	public void fromComputer(Computer computer) {
		id = computer.getId();
		name = computer.getName();
		releaseDate = computer.getIntroDate();
		discDate = computer.getDiscontDate();
		company = computer.getCompany();
		if(company != null) {
			companyId = computer.getCompany().getId();
			companyName = company.getName();
		}
		
		
		//convert java Date to sql Timestamp, ensuring date is not null
		sqlReleaseDate = (releaseDate!=null ? new java.sql.Timestamp(releaseDate.getTime()) : null);
		sqlDiscDate = (sqlDiscDate!=null ? new java.sql.Timestamp(sqlDiscDate.getTime()) : null);
	}

	public void fromResultSet(ResultSet res) throws SQLException {
		id = res.getLong(1);
		name = res.getString(2);

		sqlReleaseDate = res.getTimestamp(3);
		sqlDiscDate = res.getTimestamp(4);

		//convert sql.Date to java.util.Date, asserting date is not null.
		releaseDate = (sqlReleaseDate!=null? new Date(sqlReleaseDate.getTime()) : null);
		discDate = (sqlDiscDate!=null? new Date(sqlDiscDate.getTime()) : null);

		//get company name;
		companyId = res.getLong(5);
		companyName= res.getString(6);
	}
}
