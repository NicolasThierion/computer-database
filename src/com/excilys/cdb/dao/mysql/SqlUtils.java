package com.excilys.cdb.dao.mysql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class SqlUtils {
	/** where SQL scripts are stored */
	private static final File SQL_DIR = new File("res/sql");
	
	public static void loadSqlQuery(String sqlFileName, Map<String, String> queriesMap) throws IOException {
		File sqlFile = new File(SQL_DIR, sqlFileName);

		FileInputStream fis = null;
		BufferedReader br = null;

		//read file into a string.
		fis = new FileInputStream(sqlFile);
		br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

		StringBuilder strBuilder = new StringBuilder();
		String line;
		while(( line = br.readLine()) != null ) {
			strBuilder.append(line);
			strBuilder.append(' ');
		}

		//store this string into queries map
		queriesMap.put(sqlFileName, strBuilder.toString());

		br.close();
		fis.close();
	}
}
