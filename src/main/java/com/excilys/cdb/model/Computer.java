package com.excilys.cdb.model;

import java.io.Serializable;
import java.time.LocalDate;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

/**
 * @author Nicolas THIERION
 * @version 0.2.0
 */
@Component
public class Computer implements Serializable, Identifiable<Long> {

    /**
     *
     */
    private static final long serialVersionUID = 3247508689250542994L;

    // TODO : name is mandatory. Remove default name?
    private static final String DEFAULT_NAME = "Ordinateur sans nom";
    private static final Long DEFAULT_ID = null;

    /* ***
     * ATTRIBUTES
     */

    /** name of this computer. */
    @NotEmpty(message = "computer.error.name.empty")
    private String              mName;
    /** manufacturer of this computer. */
    private Company             mCompany;
    /** release date. */
    private LocalDate           mReleaseDate;
    /** discontinuation date. */
    private LocalDate           mDiscDate;
    /** id of this computer. */
    private Long                mId              = DEFAULT_ID;

    /* ***
     * CONSTRUCTORS
     */

    private void mNewComputer(Long id, final String name, Company manufacturer,
            LocalDate releaseDate,
            LocalDate discontinuedDate) {

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
        mReleaseDate = (releaseDate != null ? LocalDate.from(releaseDate)
                : null);
        mDiscDate = (discontinuedDate != null ? LocalDate.from(discontinuedDate) : null);
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
    public Computer(long id, String name, Company manufacturer, LocalDate releaseDate, LocalDate discontinuedDate) {
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
    public Computer(String name, Company manufacturer, LocalDate releaseDate, LocalDate discontinuedDate) {
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
    @Override
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

    /**
     * Same as {@link #getManufacturer()}.
     *
     * @return the manufacturer of this computer.
     */
    public Company getCompany() {
        return mCompany;
    }

    /**
     * Same as {@link #setManufacturer(Company)}.
     *
     * @param company
     *            The manufacturer of this computer.
     */
    public void setCompany(Company company) {
        mCompany = company;
    }

    public Company getManufacturer() {
        return getCompany();
    }

    public void setManufacturer(Company company) {
        setCompany(company);
    }

    public LocalDate getReleaseDate() {
        return mReleaseDate;
    }

    /**
     *
     * @param releaseDate
     * @throws IllegalArgumentException
     *             if date provided is posterior to discontinuation date.
     */
    public void setReleaseDate(LocalDate releaseDate) throws IllegalArgumentException {
        if (mDiscDate != null && releaseDate != null && releaseDate.compareTo(mDiscDate) > 0) {
            throw new IllegalArgumentException("release date must be prior to release date");
        }
        mReleaseDate = releaseDate;
    }

    public LocalDate getDiscontinuedDate() {
        return mDiscDate;
    }

    /**
     *
     * @param discontDate
     * @throws IllegalArgumentException
     *             if date provided is prior to release date.
     */
    public void setDiscontDate(LocalDate discontDate) throws IllegalArgumentException {

        if (mReleaseDate != null && discontDate != null && discontDate.compareTo(mReleaseDate) < 0) {
            throw new IllegalArgumentException("Discontinuation date must be posterior to release date");
        }
        mDiscDate = discontDate;
    }

    /**
     * Says if the computer is valid. A valid computer must have a positive id,
     * a non-null & non-empty name, optional release & discontinued dates must
     * be temporally coherent.
     *
     * @return true if the computer is valid.
     */
    public boolean isValid() {
        boolean res = (mId != null && mId > 0);
        if (res && mName != null) {
            res = res && !mName.trim().isEmpty();
        }
        if (res && mReleaseDate != null && mDiscDate != null) {
            res = res && mReleaseDate.compareTo(mDiscDate) < 0;
        }
        if (res && mCompany != null) {
            res = res && mCompany.isValid();
        }

        return res;
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
