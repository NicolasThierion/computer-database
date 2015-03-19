package com.excilys.cdb.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * TODO doc.
 *
 * @author Nicolas THIERION.
 *
 * @param <T>
 */
public interface EntityMapper<T> {

    void fromEntity(T entity);

    T fromResultSet(ResultSet res) throws SQLException;

}
