package com.excilys.cdb.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Company;

/**
 * DTO of Company Object. Compliant with JavaBean standard.
 *
 * @author Nicolas THIERION
 * @version 0.2.0
 */
@Component
public class CompanyDto implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = -3220828765666291714L;

    /* ***
     * ATTRIBUTES
     */

    /** name of this company. */
    @NotEmpty
    private String            mName;
    /** Name of this company. */
    private Long              mId;


    /* ***
     * CONSTRUCTORS
     */
    /**
     * Default constructor. Initialize this DTO with all fields to null.
     */
    public CompanyDto() {
        mNewCompanyDto(null, null);
    }

    private void mNewCompanyDto(Long id, String name) {
        mId = (id != null ? new Long(id) : null);
        mName = name;
    }

    /**
     * Create a new Company DTO corresponding to the given Company Object.
     *
     * @param company
     *            company to build the DTO. Can be null.
     * @return
     */
    public static CompanyDto fromCompany(Company company) {
        final CompanyDto dto = new CompanyDto();

        final Long id;
        final String name;
        if (company == null) {
            id = null;
            name = null;
        } else {
            id = company.getId();
            name = company.getName();
        }
        dto.mNewCompanyDto(id, name);

        return dto;
    }

    /**
     * Copy constructor.
     *
     * @param companyDto
     *            CompanyDto Object to copy.
     */
    public CompanyDto(CompanyDto companyDto) {
        mNewCompanyDto(companyDto.mId, companyDto.mName);
    }

    /* ***
     * GETTERS
     */
    public String getName() {
        return mName;
    }

    public Long getId() {
        return mId;
    }

    /* ***
     * SETTERS
     */
    public void setName(String name) {
        mName = name;
    }

    public void setId(Long id) {
        mId = id;
    }

    /* ***
     * Object OVERRIDES
     */

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
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CompanyDto other = (CompanyDto) obj;
        if (mId == null) {
            if (other.mId != null) {
                return false;
            }
        } else if (!mId.equals(other.mId)) {
            return false;
        }
        if (mName == null) {
            if (other.mName != null) {
                return false;
            }
        } else if (!mName.equals(other.mName)) {
            return false;
        }
        return true;
    }

}
