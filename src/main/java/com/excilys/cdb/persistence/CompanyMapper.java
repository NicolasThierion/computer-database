package com.excilys.cdb.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.cdb.model.Company;

//TODO
public class CompanyMapper implements EntityMapper<Company> {

    private Long   mId;
    private String mName;

    @Override
    public void fromEntity(Company company) {
        mName = company.getName();
        mId = company.getId();
    }

    @Override
    public Company fromResultSet(ResultSet res) throws SQLException {
        int colId = 1;
        mId = res.getLong(colId++);
        mName = res.getString(colId++);
        return new Company(mId, mName);
    }

}
