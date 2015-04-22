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

public final class SqlUtils {
    /** where SQL scripts are stored. */
    private static final String SQL_DIR = "/sql";

    private SqlUtils() {

    }

    public static void loadSqlQuery(String sqlFileName, Map<String, String> queriesMap) throws IOException {

        final InputStream fis = SqlUtils.class.getResourceAsStream(SQL_DIR + "/" + sqlFileName);
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
        queriesMap.put(sqlFileName, strBuilder.toString());

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

