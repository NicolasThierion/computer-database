package com.excilys.cdb.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Nicolas THIERION
 * @version 0.2.0
 * TODO doc
 *
 */
// TODO CompanyDaoDto
// TODO Builder
@Entity
@Table(name = "company")
public class Company implements Serializable, Identifiable<Long> {

    private static final long serialVersionUID = 1233212107449481466L;

    private static final String DEFAULT_NAME = "unknown company";

    /* ***
     * ATTRIBUTES
     */
    /** Company name. */
    @Column(name = "name")
    private String              name;
    /** Company id. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    /* ***
     * CONSTRUCTORS
     */
    /**
     * Default constructor. Create a company with id = null & name =
     * DEFAULT_NAME.
     */
    public Company() {
        this.name = DEFAULT_NAME;
        this.id = null;
    }

    /**
     * Argument constructor. Create a new company with given name & id = 0.
     *
     * @param name
     *            Name of this company.
     */
    public Company(String name) {

        this.id = null;
        setName(name);
    }

    /**
     * Argument constructor. Create a new company with given name & given id.
     *
     * @param id
     * @param name
     */
    public Company(long id, String name) throws IllegalArgumentException {
        this.id = id;
        setName(name);
    }

    /**
     * Copy constructor.
     * @param manufacturer
     */
    public Company(Company manufacturer) {
        this.name = manufacturer.name;
        this.id = manufacturer.id;
    }

    /* ***
     * ACCESSORS
     */

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Deprecated
    public boolean isValid() {
        return (id != null && id > 0 && name != null && !name.trim().isEmpty());
    }

    /* ***
     * OBJECT OVERRIDES
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(super.getClass().getSimpleName()).append(":")
.append(" : id=").append(id).append(" : name=")
                .append(name);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        return true;
    }


}
