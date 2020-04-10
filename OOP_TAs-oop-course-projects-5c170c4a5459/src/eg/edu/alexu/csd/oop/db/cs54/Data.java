package eg.edu.alexu.csd.oop.db.cs54;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import eg.edu.alexu.csd.oop.db.Database;

public class Data implements Database {

	private String path = null;
	private RegexQuery r = new RegexQuery();
	private Map<String,String> type = new HashMap<String,String>();
	@Override
	public String createDatabase(String databaseName, boolean dropIfExists) {
		// TODO Auto-generated method stub
		boolean flag = false;
		if (dropIfExists) {
			try {
				flag = executeStructureQuery("DROP DATABASE " + databaseName);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			try {
				flag = executeStructureQuery("CREATE DATABASE " + databaseName);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (flag) {
			return path;
		} else {
			throw new UnsupportedOperationException("Error");
		}
	}

	@Override
	public boolean executeStructureQuery(String query) throws SQLException {
		String key = r.matchStrQuery(query);
		boolean execute = false;
		switch(key) {
		   case "createData" :
		      path = r.createData(query);
		      r.setPath(path);
		      execute = true;
		      break; 
		   case "dropData" :
		      path = r.dropData(query);
		      r.setPath(path);
		      execute = true;
		      break;
		   case "createTable" :
			   execute = r.createTable(query);
			   type = r.getmaptype();
			      break;
		   case "dropTable" :
			  r.dropTable(query);
			  execute = true;
			      break;
		   case "BadQuery" :
			   execute = false;
			   throw new SQLException(); 
		}
	
		return execute;
	}

	@Override
	public Object[][] executeQuery(String query) throws SQLException {
		if( r.select(query).equals(null)) {
			throw new SQLException();
		}
		return r.select(query);
	}

	@Override
	public int executeUpdateQuery(String query) throws SQLException {
		String key = r.matchQuery(query);
		int done = 0;
		switch(key) {
		   case "insert" :
		      done = r.insert(query);
		      r.setPath(path);
		      if (done == -1) {
				   throw new SQLException(); 
			   }
		      break; 
		   case "update" :
			   done = r.update(query);
			   r.setPath(path);
			   if (done == -1) {
				   throw new SQLException(); 
			   }
		      break;
		   case "delete" :
			   done = r.delete(query);
			   r.setPath(path);
			   if (done == -1) {
				   throw new SQLException(); 
			   }
			      break;
		   case "BadQuery" :
			   done = -2;
		}
		return done;
	}
public Map<String,String> getmaptype(){
	return type;
}
public String getTableName(){
	return r.getTableName();
}
public String path() {
	return path;
}
}

