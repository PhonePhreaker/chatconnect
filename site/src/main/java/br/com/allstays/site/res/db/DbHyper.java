package br.com.allstays.site.res.db;

import java.net.ConnectException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;

import org.hsqldb.server.Server;

public class DbHyper {
	
	private static final int READ_WHOLE = -1;

//TODO make it so it stores in the local memory db instead of this array 
	private ArrayList<ResultSet> Cache;
	
	private Server Server;
	
	private String Dbname;
	private String DbMem;
	private Connection Connection = null;
	private Connection MemConnection = null;
	
	private DbHyper(Server server, String dbname,String memdbname, Connection memcon, Connection connection) {
		Dbname = dbname;
		DbMem = memdbname;
		MemConnection = memcon;
		Connection = connection;
		Server = server;
	}
	
	
//will create a new Db and server
	protected static DbHyper newDbHyper(boolean ssl, boolean inmem) {
		
		String sendmem = null;
		Connection sendmc = null;
		Connection sendc = null;
		Server send = new Server();
		
		send.setDatabaseName(0, "sitedb");
		send.setDatabasePath(0, "file:/home/hsqldb/sitedb");
		
		String url = "jdbc:hsqldb:hsql://localhost:9001/";
		
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
		} catch(ClassNotFoundException e) {
			
			System.err.println("Could not get JDBC Drivers");
			e.printStackTrace();
			return null;
		}
		
		if(inmem) {

			sendmem = "memsitedb";
			send.setDatabaseName(1, "memsitedb");
			send.setDatabasePath(1, "mem:memsitedb");
			send.setPort(9001);
			
//"SA" and "" are the user and password
			try {
				sendmc = DriverManager.getConnection(url+"sitedb", "SA", "");
			} catch (SQLException e) {
				System.out.println("Could not connect to driver");
				e.printStackTrace();
				return null;
			}
			
		}
		
		return new DbHyper(send, "sitedb", sendmem, sendmc, sendc);
	}

	
/*
//Connect to already runnning instance 
//TODO WARNIGN NOT YET TESTED 
	protected DbHyper(String dbname, String dbpath, boolean ssl, String user, String passwd) throws ConnectException {
		Dbname = dbname;
		DbMem = null;
		
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
		} catch (Exception e) {
			System.err.println("Could not connect to HSQLDB");
			e.printStackTrace();
			throw new ConnectException("Could not connect to HSQLDB");
		}
		
		if(ssl) {
			try {
				Connection = DriverManager.getConnection("jdbc:hsqldb:hsqls://localhost"+dbpath, user, passwd);
			} catch (SQLException e) {
				System.err.println("...Could not connect to HSQLDB with ssl at dbpath="+dbpath);
				e.printStackTrace();
			}
		}
		System.err.println("Warning connecting unsecure to HSQLDB at path"+dbpath);
		
		try {
			Connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost"+dbpath, user, passwd);
		} catch (SQLException e) {
			System.err.println("...Could not connect to hsqdl dbpath="+dbpath);
			e.printStackTrace();
		}
		
		String use = "USE "+Dbname;
		try (Statement statement = Connection.createStatement()){
			ResultSet rs = statement.executeQuery(use);
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
*/
/*
 * Will supply the names of all tables 
 */
	protected ArrayList GetDBMetadata(int format, boolean print) {
		
		ArrayList<String> names = new ArrayList();
		
		String query = "GO, SELECT *, FROM INFORMATION_SCHEMA.TABLES.VIEWS, GO";
		try (Statement statement = Connection.createStatement()) {
			
			ResultSet rs = statement.executeQuery(query);
			while(rs.next()) {
				String get;
				names.add((get = rs.getString("TABLE_NAME")));
				
				if(print) {
					System.out.println("TABLE "+get);
				}
			}
			return names;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

//Will return contents in json format the contents of a table
	protected ResultSet readTable(String db, String tbName, String collumn[], boolean cache) throws QueryException{
		
		if(db == null || tbName == null || collumn == null) {
			
			throw new QueryException("Could not get contents of table, critical arg passed as null");
		}
		
		String queryExist = "GO, USE "+db+", SELECT EXISTS(SELECT * FROM"+tbName+"), GO";
		ResultSet getExistQuery = sendQuery(queryExist, "Could not determin if a table exists ");
	
		StringBuilder builder = new StringBuilder();
		builder.append("GO, SELECT ");
		
		for(String get : collumn) {
			builder.append(get+", ");
		}
		
		builder.append("FROM "+tbName+", GO");
		return sendQuery(builder.toString(), "Could not get contents of table"+tbName);
	}
	
	protected String createTable(String db, String tbName, DbCollumn collumns[]) throws NullArgException, QueryException {
		
		if(db == null || tbName == null || collumns == null) {
			throw new NullArgException("One of the args in createTable function is null");
		}
		
		StringBuilder qbuilder = new StringBuilder();
		qbuilder.append("GO, USE "+db+", CREATE TABLE "+tbName+" (");
		
		for(int c=0; c<collumns.length; c++) {
			qbuilder.append(collumns[c].formatQuery());
		}
		qbuilder.append(");, GO,");
		ResultSet rs = sendQuery(qbuilder.toString(), "Could not send create table query");		
		
		return procSqlWarnings(rs);
	}
	
	private String procSqlWarnings(ResultSet rs) {
		try {
			
			SQLWarning warn = rs.getWarnings();
			return warn.getSQLState();
			
		} catch(SQLException e) {
			
			System.err.println("Could not load");
			e.printStackTrace();
			return null;
		}
	}
	
	private ResultSet sendQuery(String query, String exception) throws QueryException{
		if(Connection == null) {
			throw new QueryException("Connection is set to null");
		}
		try(Statement statement = Connection.createStatement()){
			return statement.executeQuery(query);
			
		}catch (SQLException e) {

			e.printStackTrace();
			throw new QueryException("SQLException: {"+query+"}"+exception);
		}
	}

}
