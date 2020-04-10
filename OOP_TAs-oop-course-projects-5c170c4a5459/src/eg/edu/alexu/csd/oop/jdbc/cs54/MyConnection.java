package eg.edu.alexu.csd.oop.jdbc.cs54;

import java.sql.SQLException;

import java.sql.Statement;

import org.apache.log4j.Logger;

public class MyConnection extends AbsractConnection{
	Statement statement;
	String databaseName;
	private boolean closed;
	final static Logger log = Logger.getLogger(MyConnection.class.getName());
	public MyConnection (String path) {

		if (path == null || path == "") {
			databaseName ="";
		} else {
			databaseName = getName(path);
		}

		closed = false;
}
	@Override
	public void close() throws SQLException {
		// TODO Auto-generated method stub
		if (closed) {
			log.error("Failed to close connection "
    				+ ": Connection is already closed");
			throw new SQLException("Failed to close Connection "
    				+ ": Connection is already closed");
		}
		closed = true;
		if (statement != null) {
			try {
				statement.close();
			} catch (Exception e) {
			}
		}

		databaseName = "";
		log.info("Connection is closed successfully");

	}

	@Override
	public Statement createStatement() throws SQLException {
		// TODO Auto-generated method stub
		log.info("Creating Statement ...");
		if (closed) {
			log.error("Failed to create statement "
    				+ ": Connection is already closed");
			throw new SQLException("Failed to create statement "
    				+ ": Connection is already closed");
		}
		log.info("Statement is created successfully");
        return new MyStatement(this, databaseName);
		
	}
	private String getName (String path) {
		String[] temp = null;
		if (path.contains("/")) {
			temp = path.split("/");
		} else {
			temp = path.split("\\\\");
		}
		return temp[temp.length - 1];
}
}
