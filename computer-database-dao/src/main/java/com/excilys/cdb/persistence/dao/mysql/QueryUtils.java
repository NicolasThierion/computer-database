package com.excilys.cdb.persistence.dao.mysql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import com.excilys.cdb.persistence.ConnectionFactory;

public final class QueryUtils {
    /**
     * where SQL scripts are stored.
     *
     * @deprecated should now use HQL queries instead of SQL ones.
     */
    @Deprecated
    private static final String SQL_DIR = "/sql";

    /** where HQL scripts are stored. */
    private static final String HQL_DIR = "/hql";

    private QueryUtils() {

    }

    // TODO remove SQL support
    public static void loadQuery(String sqlOrHqlFileName, Map<String, String> queriesMap) throws IOException {

        InputStream fis = null;
        if (sqlOrHqlFileName.endsWith(".sql")) {
            fis = QueryUtils.class.getResourceAsStream(SQL_DIR + "/" + sqlOrHqlFileName);
        } else if (sqlOrHqlFileName.endsWith(".hql")) {
            fis = QueryUtils.class.getResourceAsStream(HQL_DIR + "/" + sqlOrHqlFileName);
        }
        BufferedReader br = null;

        // read file into a string.
        br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

        final StringBuilder strBuilder = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            strBuilder.append(line);
            strBuilder.append(' ');
        }

        // store this string into queries map
        queriesMap.put(sqlOrHqlFileName, strBuilder.toString());

        br.close();
        fis.close();
    }

    private static void safeCloseClosable(AutoCloseable closable) {
        if (closable == null) {
            return;
        }
        try {
            closable.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void safeCloseResult(ResultSet result) {
        safeCloseClosable(result);
    }

    public static void safeCloseConnection(Connection conn) {
        ConnectionFactory.getInstance().close(conn);
    }

    public static void safeCloseStatement(Statement statement) {
        safeCloseClosable(statement);
    }

    public static void safeCloseAll(Connection conn, Statement statement, ResultSet result) {
        safeCloseResult(result);
        safeCloseStatement(statement);
        safeCloseConnection(conn);
    }
}

