package com.excilys.cdb.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Nicolas THIERION
 * @version 0.2.0 TODO : The class is Serializable according to JavaBean
 *          standard, for eventual future needs.
 */
public class Computer implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3247508689250542994L;

    // TODO : name is mandatory. Remove default name?
    private static String DEFAULT_NAME = "Ordinateur sans nom";
    private static Long DEFAULT_ID = null;

    /* ***
     * ATTRIBUTES
     */

    /** name of this computer. */
    private String mName;
    /** manufacturer of this computer. */
    private Company mCompany;
    /** release date. */
    private LocalDateTime mReleaseDate;
    /** discontinuation date. */
    private LocalDateTime mDiscDate;
    /** id of this computer. */
    private Long mId = DEFAULT_ID;

    /* ***
     * CONSTRUCTORS
     */

    private void mNewComputer(Long id, final String name, Company manufacturer,
            LocalDateTime releaseDate, LocalDateTime discontinuedDate) {
        // check name
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Computer name should not be null or empty");
        }

        // check dates
        if (releaseDate != null && discontinuedDate != null) {
            if (releaseDate.compareTo(discontinuedDate) > 0) {
                throw new IllegalArgumentException(
                        "Release date must be prior to discontinuation date");
            }
        }

        // set attributes
        mName = name;
        mId = id;
        mCompany = (manufacturer != null ? new Company(manufacturer) : null);
        mReleaseDate = (releaseDate != null ? LocalDateTime.from(releaseDate)
                : null);
        mDiscDate = (discontinuedDate != null ? LocalDateTime
                .from(discontinuedDate) : null);
    }

    /**
     * Create a new computer with a Null Company, null dates, default computer
     * name & computer id=null.
     */
    public Computer() {
        mNewComputer(DEFAULT_ID, DEFAULT_NAME, null, null, null);
    }

    /**
     * Create a new computer with given name, a Null Company, null dates &
     * computer id=null.
     * 
     * @param name
     *            Name of this computer.
     */
    public Computer(final String name) {
        mNewComputer(DEFAULT_ID, name, null, null, null);
    }

    /**
     * Copy constructor.
     * 
     * @param computer
     *            Computer object to copy.
     */
    public Computer(final Computer computer) {
        final Computer o = computer;
        mNewComputer(o.mId, o.mName, o.mCompany, o.mReleaseDate, o.mDiscDate);
    }

    /**
     * Create a new computer with given name, given computer ID, a Null Company
     * & null dates.
     * 
     * @param id
     *            id of this computer.
     * @param name
     *            Name of this computer.
     * @throws IllegalArgumentException
     *             if parameter 'name' is null or empty, or if id provided is
     *             invalid.
     */
    public Computer(long id, String name) throws IllegalArgumentException {
        if (id < 1) {
            throw new IllegalArgumentException("Provided id=" + id
                    + " is invalid");
        } else if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(
                    "Computer name must not be empty");
        }

        mNewComputer(id, name, null, null, null);
    }

    /**
     * Create a computer with given parameters.
     * 
     * @param id
     *            id of this computer.
     * @param name
     *            Name of this computer.
     * @param manufacturer
     *            company that manufactured this computer.
     * @param releaseDate
     *            when this computer has been released.
     * @param discontinuedDate
     *            when this computer has been discontinued.
     */
    public Computer(long id, String name, Company manufacturer,
            LocalDateTime releaseDate, LocalDateTime discontinuedDate) {
        mNewComputer(id, name, manufacturer, releaseDate, discontinuedDate);
    }

    /**
     * Create a new computer with NULL computer id & given parameters.
     * 
     * @param name
     *            Name of this computer.
     * @param manufacturer
     *            Company that manufactured this computer.
     * @param releaseDate
     *            when this computer has been released.
     * @param discontinuedDate
     *            when this computer has been discontinued.
     */
    public Computer(String name, Company manufacturer,
            LocalDateTime releaseDate, LocalDateTime discontinuedDate) {
        mNewComputer(DEFAULT_ID, name, manufacturer, releaseDate,
                discontinuedDate);
    }

    /* ***
     * ACCESSORS
     */
    /**
     * May return null if this is a new computer, that has not yet been added to
     * database.
     * 
     * @return
     */
    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    /**
     * @param name
     * @throws IllegalArgumentException
     *             if parameter 'name' is null or empty.
     */
    public void setName(String name) throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Computer name must not be empty");
        }
        mName = name;
    }

    public Company getCompany() {
        return mCompany;
    }

    public void setCompany(Company company) {
        mCompany = company;
    }

    public LocalDateTime getIntroDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        mReleaseDate = releaseDate;
    }

    public LocalDateTime getDiscontDate() {
        return mDiscDate;
    }

    public void setDiscontDate(LocalDateTime discontDate) {
        mDiscDate = discontDate;
    }

    /* ***
     * OBJECT's OVERRIDES
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(getClass().getSimpleName()).append(" : ").append("id=")
        .append(mId).append(" ; name=").append(mName);
        sb.append(" ; manufacturer=").append(mCompany);
        sb.append(" ; released=").append(mReleaseDate)
        .append(" ; discontinued=").append(mDiscDate);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((mCompany == null) ? 0 : mCompany.hashCode());
        result = prime * result
                + ((mDiscDate == null) ? 0 : mDiscDate.hashCode());
        result = prime * result + ((mId == null) ? 0 : mId.hashCode());
        result = prime * result + ((mName == null) ? 0 : mName.hashCode());
        result = prime * result
                + ((mReleaseDate == null) ? 0 : mReleaseDate.hashCode());
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

        final Computer other = (Computer) obj;
        if (mCompany == null) {
            if (other.mCompany != null) {
                return false;
            }
        } else if (!mCompany.equals(other.mCompany)) {
            return false;
        }
        if (mDiscDate == null) {
            if (other.mDiscDate != null) {
                return false;
            }
        } else if (!mDiscDate.equals(other.mDiscDate)) {
            return false;
        }
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
        if (mReleaseDate == null) {
            if (other.mReleaseDate != null) {
                return false;
            }
        } else if (!mReleaseDate.equals(other.mReleaseDate)) {
            return false;
        }
        return true;
    }

}
