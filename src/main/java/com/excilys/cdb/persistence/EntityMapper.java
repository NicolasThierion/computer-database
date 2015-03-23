package com.excilys.cdb.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps an entity from its database representation to its Object form. ie :
 * loads the fiels from a ResultSet & create the corresponding Model Object.
 *
 * @author Nicolas THIERION.
 *
 * @param <T>
 */
public interface EntityMapper<T> {

    /**
     * Creates a new EntityMapper with its fields initialized from the given
     * entity fieds.
     *
     * @param entity
     *            Model entity.
     */
    void fromEntity(T entity);

    /**
     * Creates a new EntityMapper with its fields initialized from the given
     * ResultSet.
     *
     * @param res
     * @return The created entity.
     * @throws SQLException
     *             if there was an error while parsing the given resultSet. (IE
     *             : resultset is not of expected format).
     */
    T fromResultSet(ResultSet res) throws SQLException;

}
