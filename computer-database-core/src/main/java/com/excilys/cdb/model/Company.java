package com.excilys.cdb.model;

import java.io.Serializable;

import org.springframework.stereotype.Component;

/**
 *
 * @author Nicolas THIERION
 * @version 0.2.0
 * TODO doc
 *
 */
@Component
public class Company implements Serializable, Identifiable<Long> {

    private static final long serialVersionUID = 1233212107449481466L;

    private static final String DEFAULT_NAME = "unknown company";

    /* ***
     * ATTRIBUTES
     */
    /** Company name. */
    private String mName;
    /** Company id. */
    private Long mId;

    /* ***
     * CONSTRUCTORS
     */
    /**
     * Default constructor. Create a company with id = null & name =
     * DEFAULT_NAME.
     */
    public Company() {
        mName = DEFAULT_NAME;
        mId = null;
    }

    /**
     * Argument constructor. Create a new company with given name & id = 0.
     *
     * @param name
     *            Name of this company.
     */
    public Company(String name) {

        mId = null;
        setName(name);
    }

    /**
     * Argument constructor. Create a new company with given name & given id.
     *
     * @param id
     * @param name
     */
    public Company(long id, String name) throws IllegalArgumentException {
        mId = id;
        setName(name);
    }

    /**
     * Copy constructor.
     * @param manufacturer
     */
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

    @Override
    public Long getId() {
        return mId;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public boolean isValid() {
        return (mId != null && mId > 0 && mName != null && !mName.trim().isEmpty());
    }

    /* ***
     * OBJECT OVERRIDES
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(super.getClass().getSimpleName()).append(":")
        .append(" : id=").append(mId).append(" : name=").append(mName);
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
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Company other = (Company) obj;
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
