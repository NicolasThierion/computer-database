package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.SQLException;

public class Transaction {

    /* ***
     * ATTRIBUTES
     */
    private final Connection mConn;

    /* ***
     * CONSTRUCTOR
     */
    /**
     * Inactive transaction. Must call {@link #begin()} to start this
     * transaction.
     *
     * @param conn
     *            connection this transaction should use.
     */
    public Transaction(Connection conn) {
        mConn = conn;
    }

    @Override
    public void finalize() {
        try {
            end();
            mConn.close();
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    public void begin() throws SQLException {
        mConn.setAutoCommit(false);
    }

    public void end() throws SQLException {
        mConn.setAutoCommit(true);

    }

    public void commit() throws SQLException {
        mConn.commit();
    }

    public void rollback() throws SQLException {
        mConn.rollback();
    }

    public boolean isStarted() {
        try {
            return !mConn.getAutoCommit();
        } catch (final SQLException e) {
            return false;
        }
    }

    public Connection getConnection() {
        return mConn;
    }

}
