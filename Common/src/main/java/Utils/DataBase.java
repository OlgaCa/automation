package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataBase {

	public enum DatabaseType {Online, Offline, PaisaDB,PaisaVaultDB,PaisaCmsDB, MoneyDB,MoneyVaultDB, MoneyCmsDB};

	/**
	 * Creates database connection using the Config parameters - 'DBConnectionString', 'DBConnectionUsername' and 'DBConnectionPassword'
	 * @param Config test config instance
	 * @return Db Connection
	 */
	private static Connection getConnection(Config testConfig, DatabaseType dbType)
	{
		Connection con = null;
		if(testConfig.connection == null)
		{

			String connectString = null;
			String userName = null;
			String password = null;
			try{
				switch(dbType)
				{
				case Online:
					connectString = testConfig.getRunTimeProperty("OnlineDBConnectionString");
					testConfig.logComment("Connecting to ONLINE db:-" + connectString);
					break;
				case Offline:
					connectString = testConfig.getRunTimeProperty("OfflineDBConnectionString");
					testConfig.logComment("Connecting to OFFLINE db:-" + connectString);
					break;
				case PaisaDB:
				case MoneyDB:
					connectString = testConfig.getRunTimeProperty("PaisaDBConnectionString");
					testConfig.logComment("Connecting to Money db:-" + connectString);
					break;
				case PaisaVaultDB:
				case MoneyVaultDB:
					connectString = testConfig.getRunTimeProperty("PaisaVaultDBConnectionString");
					testConfig.logComment("Connecting to Money db:-" + connectString);
					break;
				case PaisaCmsDB:
				case MoneyCmsDB:
					connectString = testConfig.getRunTimeProperty("PaisaCMSDBConnectionString");
					testConfig.logComment("Connecting to Money db:-" + connectString);
					break;
				}

				if(dbType ==DatabaseType.PaisaDB || dbType==DatabaseType.PaisaVaultDB || dbType==DatabaseType.PaisaCmsDB) {
					userName = testConfig.getRunTimeProperty("PaisaDBConnectionUsername");
					password = testConfig.getRunTimeProperty("PaisaDBConnectionPassword");
				}

				else {
					userName = testConfig.getRunTimeProperty("DBConnectionUsername");
					password = testConfig.getRunTimeProperty("DBConnectionPassword");
				}


				try 
				{
					Class.forName(testConfig.getRunTimeProperty("DBConnectionDriver"));
				} 
				catch (ClassNotFoundException e) 
				{
					con=null;
					testConfig.logException(e);
				}
				con=DriverManager.getConnection(connectString, userName, password);
				testConfig.logComment("Connection succeeded");
			}
			catch(SQLException e)
			{
				testConfig.logException(e);
			}

			//testConfig.connection = con;
		}

		return con;
	}

	/**
	 * Executes the select db query, and saves the result in Config.runtimeProperties as well as returns Map
	 * @param Config test config instance
	 * @param sqlRow row number of the 'Query' column of 'SQL' sheet of Test data excel having the query to be executed
	 * @param rowNumber row number to be returned (use 1 for first row and -1 for last row) 
	 * @return Map containing key:value pairs of specified row
	 */
	public static Map<String,String> executeSelectQuery(Config testConfig, int sqlRow, int rowNumber, DatabaseType dbType)
	{	
		//Read the Query column of SQL sheet of Test data excel
		TestDataReader sqlData = new TestDataReader(testConfig, "SQL");
		String selectQuery = sqlData.GetData(sqlRow, "Query");
		selectQuery=Helper.replaceArgumentsWithRunTimeProperties(testConfig, selectQuery);
		Log.Comment("Executing the query - '" + selectQuery +"'", testConfig);
		return executeSelectQuery(testConfig, selectQuery, rowNumber, dbType);
	}

	/**
	 * Executes the select db query for OFFLINE db, and saves the result in Config.runtimeProperties as well as returns Map
	 * @param Config test config instance
	 * @param sqlRow row number of the 'Query' column of 'SQL' sheet of Test data excel having the query to be executed
	 * @param rowNumber row number to be returned (use 1 for first row and -1 for last row) 
	 * @return Map containing key:value pairs of specified row
	 */
	public static Map<String,String> executeSelectQuery(Config testConfig, int sqlRow, int rowNumber)
	{	
		//Read the Query column of SQL sheet of Test data excel
		TestDataReader sqlData = new TestDataReader(testConfig, "SQL");
		String selectQuery = sqlData.GetData(sqlRow, "Query");
		selectQuery=Helper.replaceArgumentsWithRunTimeProperties(testConfig, selectQuery);
		Log.Comment("Executing the query - '" + selectQuery +"'", testConfig);
		return executeSelectQuery(testConfig, selectQuery, rowNumber, DatabaseType.Offline);
	}

	/**
	 * Executes the select db query, and saves the result in Config.runtimeProperties as well as returns Map
	 * @param Config test config instance
	 * @param selectQuery query to be executed
	 * @param rowNumber row number to be returned (use 1 for first row and -1 for last row) 
	 * @return Map containing key:value pairs of specified row
	 */
	public static Map<String,String> executeSelectQuery(Config testConfig, String selectQuery, int rowNumber, DatabaseType dbType){
		selectQuery=Helper.replaceArgumentsWithRunTimeProperties(testConfig, selectQuery);

		Statement stmt=null;
		ResultSet resultSet=null;
		try{
			stmt=getConnection(testConfig, dbType).createStatement();
			resultSet=stmt.executeQuery(selectQuery);
		}
		catch(SQLException e){
			testConfig.logException(e);
		}

		Map<String,String> resultMap=null;

		int row=1;
		try{
			if(rowNumber == -1)
			{
				if(resultSet.last()) 
					resultMap=addToRunTimeProperties(testConfig, resultSet);
			}
			else
			{
				while(resultSet.next())
				{
					if(row==rowNumber)
					{
						resultMap=addToRunTimeProperties(testConfig, resultSet);
						break;
					}
					else
					{
						row++;
					}
				}
			}
		}
		catch(SQLException e){
			testConfig.logException(e);
		}
		finally{
			try{
				resultSet.close();
				stmt.close();
			}catch (SQLException e) {
				testConfig.logException(e);
			}
		}

		if(null == resultMap) testConfig.logWarning("No data was returned for this query");
		return resultMap;
	}

	private static Map<String,String> addToRunTimeProperties(Config testConfig, ResultSet sqlResultSet)  
	{  
		HashMap<String,String> mapData = new HashMap<String, String>();	   

		try{
			ResultSetMetaData meta=sqlResultSet.getMetaData();
			for(int col=1; col <= meta.getColumnCount(); col++)
			{
				try{
					mapData.put(meta.getColumnLabel(col), sqlResultSet.getObject(col).toString());
				}catch(Exception e){
					mapData.put(meta.getColumnLabel(col), "");
				}
			}
		}
		catch(SQLException e){	
			testConfig.logException(e);
		}

		Set<String> keys=mapData.keySet();
		for(String key:keys){
			testConfig.putRunTimeProperty(key,mapData.get(key));
		}
		return mapData;
	}

	/**
	 * Executes the select db query, and saves the result in Config.runtimeProperties as well as returns Map
	 * @param Config test config instance
	 * @param sqlRow row number of the 'Query' column of 'SQL' sheet of Test data excel having the query to be executed
	 * @param rowNumber row number to be returned (use 1 for first row and -1 for last row) 
	 * @return Map containing key:value pairs of specified row
	 */
	public static int executeUpdateQuery(Config testConfig, int sqlRow, DatabaseType dbType)
	{	
		//Read the Query column of SQL sheet of Test data excel
		TestDataReader sqlData = new TestDataReader(testConfig, "SQL");
		String updateQuery = sqlData.GetData(sqlRow, "Query");

		updateQuery=Helper.replaceArgumentsWithRunTimeProperties(testConfig, updateQuery);

		Log.Comment("Executing the update query - '" + updateQuery +"'", testConfig);
		return executeUpdateQuery(testConfig, updateQuery, dbType);
	}

	/**
	 * Executes the update db query
	 * @param Config test config instance
	 * @param updateQuery query to be executed
	 * @return number of rows affected
	 */
	public static int executeUpdateQuery(Config testConfig, String updateQuery, DatabaseType dbType){
		Statement stmt=null;
		int rows=0;
		try{
			stmt=getConnection(testConfig, dbType).createStatement();
			updateQuery=Helper.replaceArgumentsWithRunTimeProperties(testConfig, updateQuery);
			rows=stmt.executeUpdate(updateQuery);		
		}catch(SQLException e){
			testConfig.logException(e);
		}finally{
			if(stmt!=null){
				try {stmt.close();} catch (SQLException e){testConfig.logException(e);}
			}
		}
		if(0==rows)  testConfig.logWarning("No rows were updated by this query");
		return rows;
	}

}