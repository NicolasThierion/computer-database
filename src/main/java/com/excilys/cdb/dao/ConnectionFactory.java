package com.excilys.cdb.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.sf.log4jdbc.ConnectionSpy;

import com.excilys.cdb.dao.DaoException.ErrorType;

/**
 * Data access object singleton. Establish connections to BDD. Use required
 * creditentials to connect to computer-database-db through JDBC MySQL.
 *
 * @author Nicolas THIERION
 * @version 0.2.0
 */
public final class ConnectionFactory {

    /* ***
     * DB PARAMETERS
     */
    /** Url of JDBC Driver. */
    private static final String DB_DRIVER_PACKAGE = "com.mysql.jdbc.Driver";
    /** DB name. */
    private static final String DB_NAME = "computer-database-db";
    /** DB host. */
    private static final String DB_HOST = "localhost";
    /** DB port. */
    private static final String DB_PORT = "3306";
    /** JDBC connection URL. */
    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":"
            + DB_PORT + "/" + DB_NAME;

    /** where DB creditentials are stored. */
    private static final String PROPERTIES_FILENAME = "mysql.properties";

    /* ***
     * ATTRIBUTES
     */

    /** singleton instance. */
    private static ConnectionFactory mInstance;
    /** list of DB connections provided by this factory. */
    private List<Connection> mConnections;
    /** DB connection information. */
    private Properties mProperties;

    /* ***
     * CONSTRUCTORS / DESTRUCTORS
     */

    /**
     *
     * @return
     * @throws DaoException
     */
    public static ConnectionFactory getInstance() throws DaoException {
        synchronized (ConnectionFactory.class) {
            if (mInstance == null) {
                mInstance = new ConnectionFactory();
            }
        }
        return mInstance;
    }

    /**
     *
     */
    private ConnectionFactory() {
        try {
            // init sql drivers
            mConnections = new ArrayList<Connection>();
            Class.forName(DB_DRIVER_PACKAGE).newInstance();

            // load connection properties
            mProperties = new Properties();
            mProperties.load(getClass().getClassLoader().getResourceAsStream(
                    PROPERTIES_FILENAME));

        } catch (InstantiationException | IllegalAccessException
                | ClassNotFoundException e) {
            throw new DaoException(e.getMessage(), ErrorType.DAO_ERROR);
        } catch (final IOException e) {
            throw new DaoException(e.getMessage(), ErrorType.DAO_ERROR);
        }
    }

    /**
     * Close the connection to the DB & free up memory.
     */
    public void destroy() {

        try {
            for (final Connection conn : mConnections) {
                conn.close();
            }
        } catch (final SQLException e) {
            throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
        }
    }

    @Override
    public void finalize() {
        destroy();
    }

    /* ***
     * ACCESSORS
     */

    /**
     *
     * Same as {@link #open()}.
     *
     * @return the opened connection.
     * @throws DaoException
     */
    public Connection getConnection() throws DaoException {
        return open();
    }

    /**
     * Get a connection to the BD.
     *
     * @throws DaoException
     *             if cannot establish connection to DB.
     */
    public Connection open() throws DaoException {
        try {
            Connection conn = DriverManager.getConnection(DB_URL,
                    mProperties);
            conn = new ConnectionSpy(conn);
            mConnections.add(conn);
            return conn;
        } catch (final SQLException e) {
            throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
        }
    }

    /**
     *
     * @param conn
     */
    public void close(Connection conn) {
        try {
            conn.close();
            mConnections.remove(conn);
        } catch (final SQLException e) {
            throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
        }
    }

}
