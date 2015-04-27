package com.excilys.cdb.persistence.dao;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.EntityField;


/**
 * Data access object interface. Establish connection to BDD, & offers several
 * services.
 *
 * @author Nicolas THIERION
 * @version 0.3.0
 */
public interface ICompanyDao extends ICrudDao<Company> {

    public enum CompanyField implements EntityField<Company> {
        ID("company.id"), NAME("company.name");

        String mLabel;

        CompanyField(String label) {
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
}
