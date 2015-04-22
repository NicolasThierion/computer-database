package com.excilys.cdb.persistence.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.EntityField;

/**
 * TODO doc.
 *
 * @author Nicolas THIERION.
 *
 */
public class CompanyMapper implements EntityMapper<Company> {

    /* ***
     * ATRIBUTES
     */
    private Long   mId;
    private String mName;

    /* ***
     * COLUMN ENUM
     */
    public enum Field implements EntityField<Company> {
        ID("company.id"), NAME("company.name");

        String mLabel;

        Field(String label) {
            mLabel = label;
        }

        @Override
        public String toString() {
            return mLabel;
        }

        @Override
        public String getLabel() {
            return mLabel;
        }
    }

    @Override
    public void fromEntity(Company company) {
        mName = company.getName();
        mId = company.getId();
    }

    @Override
    public Company fromResultSet(ResultSet res) throws SQLException {
        // TODO use named columns
        mId = res.getLong(Field.ID.getLabel());
        mName = res.getString(Field.NAME.getLabel());
        return new Company(mId, mName);
    }

    public Long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

}
