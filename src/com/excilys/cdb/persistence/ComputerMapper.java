package com.excilys.cdb.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

public class ComputerMapper {

	/** name of this computer */
	public String name;
	/** manufacturer of this computer */
	public Company company;
	/** release date */
	public LocalDateTime releaseDate;
	/** discontinuation date */
	public LocalDateTime discDate;
	/** id of this computer. Remember to test if not null!!!! */
	public Long id;

	public Timestamp sqlReleaseDate;
	public Timestamp sqlDiscDate;
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
		sqlReleaseDate = (releaseDate!=null ? Timestamp.valueOf(releaseDate) : null);
		sqlDiscDate = (discDate!=null ? Timestamp.valueOf(discDate) : null);
	}

	public void fromResultSet(ResultSet res) throws SQLException {
		id = res.getLong(1);
		name = res.getString(2);

		sqlReleaseDate = res.getTimestamp(3);
		sqlDiscDate = res.getTimestamp(4);

		//convert sql.Date to java.util.Date, asserting date is not null.
		releaseDate = (sqlReleaseDate!=null ? sqlReleaseDate.toLocalDateTime() : null);
		discDate = (sqlDiscDate!=null ? sqlDiscDate.toLocalDateTime() : null);

		//get company name;
		companyId = res.getLong(5);
		companyName= res.getString(6);
	}
}
