package com.excilys.cdb.persistence.dao;

import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.EntityField;


/**
 * Data access object interface. Establish connection to BDD, & offers several
 * services.
 *
 * @author Nicolas THIERION
 * @version 0.3.0
 */
@Transactional
public interface IComputerDao extends ICrudDao<Long, Computer> {


    public enum ComputerField implements EntityField<Computer> {

        ID("computer.id"), NAME("computer.name"), INTRODUCED("computer.introduced"), DISCONTINUED(
                "computer.discontinued"), COMPANY_ID("company.id");

        String mLabel;

        ComputerField(String label) {
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
