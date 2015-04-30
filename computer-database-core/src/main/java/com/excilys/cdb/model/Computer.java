package com.excilys.cdb.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import utils.LocalDateToTimestampConverter;

/**
 * @author Nicolas THIERION
 * @version 0.2.0
 */

// TODO Builder
// TODO ComputerDaoDto
@Entity
@Table(name = "computer")
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
    @Column(name = "name")
    private String              name;
    /** manufacturer of this computer. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company             company;
    /** release date. */
    @Column(name = "introduced")
    @Convert(converter = LocalDateToTimestampConverter.class)
    private LocalDate           introduced;
    /** discontinuation date. */
    @Column(name = "discontinued")
    @Convert(converter = LocalDateToTimestampConverter.class)
    private LocalDate           discontinued;
    /** id of this computer. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long                id              = DEFAULT_ID;

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
        this.name = name;
        this.id = id;
        this.company = (manufacturer != null ? new Company(manufacturer) : null);
        this.introduced = (releaseDate != null ? LocalDate.from(releaseDate)
                : null);
        this.discontinued = (discontinuedDate != null ? LocalDate.from(discontinuedDate) : null);
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
        mNewComputer(o.id, o.name, o.company, o.introduced, o.discontinued);
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
     * GETTERS
     */
    /**
     * May return null if this is a new computer, that has not yet been added to
     * database.
     *
     * @return
     */
    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * Same as {@link #getManufacturer()}.
     *
     * @return the manufacturer of this computer.
     */
    public Company getCompany() {
        return company;
    }

    public Company getManufacturer() {
        return getCompany();
    }

    /**
     * Get introduced date.
     *
     * @return
     */
    public LocalDate getIntroduced() {
        return getReleaseDate();
    }

    /**
     * @deprecated @see {@link #getIntroduced()}
     * @return
     */
    @Deprecated
    public LocalDate getReleaseDate() {
        return introduced;
    }

    public LocalDate getDiscontinued() {
        return discontinued;
    }

    /**
     * @deprecated @see {@link #getDiscontinued()}
     * @return
     */
    @Deprecated
    public LocalDate getDiscontinuedDate() {
        return getDiscontinued();
    }

    /* ***
     * SETTERS
     */

    public void setId(Long id) {
        this.id = id;
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
        this.name = name;
    }

    /**
     * Same as {@link #setManufacturer(Company)}.
     *
     * @param company
     *            The manufacturer of this computer.
     */
    public void setCompany(Company company) {
        this.company = company;
    }

    public void setManufacturer(Company company) {
        setCompany(company);
    }


    /**
     *
     * @param introduced
     * @throws IllegalArgumentException
     *             if date provided is posterior to discontinued date.
     */
    public void setIntroduced(LocalDate introduced) throws IllegalArgumentException {
        if (discontinued != null && introduced != null && introduced.compareTo(discontinued) > 0) {
            throw new IllegalArgumentException("release date must be prior to release date");
        }
        this.introduced = introduced;
    }

    /**
     * @deprecated @see {@link #setIntroduced(LocalDate)}
     * @param releaseDate
     * @throws IllegalArgumentException
     */
    @Deprecated
    public void setReleaseDate(LocalDate releaseDate) throws IllegalArgumentException {
        setIntroduced(releaseDate);
    }

    /**
     *
     * @param discontDate
     * @throws IllegalArgumentException
     *             if date provided is prior to release date.
     */
    public void setDiscontinued(LocalDate discontinued) throws IllegalArgumentException {
        if (introduced != null && discontinued != null && discontinued.compareTo(introduced) < 0) {
            throw new IllegalArgumentException("Discontinuation date must be posterior to release date");
        }
        this.discontinued = discontinued;
    }

    /**
     * @deprecated @see {@link #setDiscontinued(LocalDate)}
     * @param discontDate
     * @throws IllegalArgumentException
     */
    @Deprecated
    public void setDiscontinuedDate(LocalDate discontDate) throws IllegalArgumentException {
        setDiscontinued(discontDate);
    }

    /**
     * Says if the computer is valid. A valid computer must have a positive id,
     * a non-null & non-empty name, optional release & discontinued dates must
     * be temporally coherent.
     *
     * @deprecated better use {@link #ComputerValidator}
     * @return true if the computer is valid.
     */
    @Deprecated
    public boolean isValid() {
        boolean res = (id != null && id > 0);
        if (res && name != null) {
            res = res && !name.trim().isEmpty();
        }
        if (res && introduced != null && discontinued != null) {
            res = res && introduced.compareTo(discontinued) < 0;
        }
        if (res && company != null) {
            res = res && company.isValid();
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
        .append(id).append(" ; name=").append(name);
        sb.append(" ; manufacturer=").append(company);
        sb.append(" ; released=").append(introduced)
        .append(" ; discontinued=").append(discontinued);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((company == null) ? 0 : company.hashCode());
        result = prime * result
                + ((discontinued == null) ? 0 : discontinued.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((introduced == null) ? 0 : introduced.hashCode());
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
        if (company == null) {
            if (other.company != null) {
                return false;
            }
        } else if (!company.equals(other.company)) {
            return false;
        }
        if (discontinued == null) {
            if (other.discontinued != null) {
                return false;
            }
        } else if (!discontinued.equals(other.discontinued)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (introduced == null) {
            if (other.introduced != null) {
                return false;
            }
        } else if (!introduced.equals(other.introduced)) {
            return false;
        }
        return true;
    }

}
