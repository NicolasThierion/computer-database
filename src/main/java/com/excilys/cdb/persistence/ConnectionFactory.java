package com.excilys.cdb.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import net.sf.log4jdbc.ConnectionSpy;

import com.excilys.cdb.persistence.dao.DaoException;
import com.excilys.cdb.persistence.dao.DaoException.ErrorType;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

/**
 * Data access object singleton. Establish connections to BDD. Use required
 * credentials to connect to computer-database-db through JDBC MySQL.
 *
 * @author Nicolas THIERION
 * @version 0.2.0
 */
public final class ConnectionFactory {

    /* ***
     * DB PARAMETERS
     */
    /** Uri of JDBC Driver. */
    private static final String      DB_DRIVER_PACKAGE       = "com.mysql.jdbc.Driver";
    /** JDBC connection URL. */
    private static final String      DB_URL                  = "jdbc:mysql://%s:%s/%s";
    /** where DB creditentials are stored. */
    private static final String      PROPERTIES_FILENAME     = "mysql.properties";
    private static final String      PROPERTIES_FILENAME_ALT = "env-mysql.properties";
    /** Pool config. */
    private static final int         POOL_MIN_CONNECTIONS    = 5;
    private static final int         POOL_MAX_CONNECTIONS    = 10;
    private static final int         POOL_PARTITION_COUNT    = 10;

    /* ***
     * ATTRIBUTES
     */
    /** singleton instance. */
    private static ConnectionFactory mInstance;
    /** DB connection information. */
    private Properties               mProperties;
    /** Pool of db connections. */
    private BoneCP                   mConnections;

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
     * Create this connection factory. Initialize DB credentials & connection
     * pool.
     */
    private ConnectionFactory() throws DaoException {
        try {
            // load connection properties
            mLoadProperties();
            // EOloadConfigFile();

            // init sql drivers
            Class.forName(DB_DRIVER_PACKAGE).newInstance();
            // create connections pool
            mInitConnectionPool();
            // EOpoolInit();
        } catch (final Exception e) {
            throw new DaoException(e.getMessage(), ErrorType.DAO_ERROR);
        }
    }

    /**
     * Close the connection to the DB & free up memory.
     */
    public void destroy() {
        mConnections.shutdown();
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
            Connection conn = mConnections.getConnection();
            conn = new ConnectionSpy(conn);
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
        } catch (final SQLException e) {
            throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
        }
    }

    /* ***
     * PRIVATE METHODS
     */
    /**
     * Load properties file to get DB credentials.
     *
     * @throws IOException
     */
    private void mLoadProperties() throws IOException {
        mProperties = new Properties();

        InputStream propertyStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILENAME_ALT);
        if (propertyStream == null) {
            propertyStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILENAME);
        }
        mProperties.load(propertyStream);
    }

    /**
     * try to init connection pool. Property file must have been loaded.
     *
     * @throws Exception
     */
    private void mInitConnectionPool() throws Exception {

        final String dbName = mProperties.getProperty("db");
        final String host = mProperties.getProperty("host");
        final String port = mProperties.getProperty("port");
        final String dbUrl = String.format(DB_URL, host, port, dbName);
        final BoneCPConfig config = new BoneCPConfig();
        config.setDriverProperties(mProperties);
        config.setJdbcUrl(dbUrl);
        config.setMinConnectionsPerPartition(POOL_MIN_CONNECTIONS);
        config.setMaxConnectionsPerPartition(POOL_MAX_CONNECTIONS);
        config.setPartitionCount(POOL_PARTITION_COUNT);
        mConnections = new BoneCP(config);
    }

}
