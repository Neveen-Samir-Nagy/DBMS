package eg.edu.alexu.csd.oop.jdbc.cs54;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import eg.edu.alexu.csd.oop.db.cs54.Data;

public class MyStatement extends AbsractStatement {
	private Data dbms;
	private Connection connection;
	private List<String> batch;
	private String dbName;
	private int timeOut;
	boolean closed;
	Object[][] selected;
	private MyResultset resultSet;
	final static Logger log = Logger.getLogger(MyStatement.class.getName());

	public MyStatement(Connection con, String databaseName) {

		connection = con;
		dbName = databaseName;
		batch = new ArrayList<String>();
		dbms = new Data();
		timeOut = 0;
		closed = false;
		selected = null;

		if (dbName != "") {
			try {
				execute("Create database " + dbName);
			} catch (SQLException e) {
				throw new RuntimeException("can't create database in statement constructor");
			}
		}

	}

	@Override
	public void addBatch(String sql) throws SQLException {
		// TODO Auto-generated method stub
		log.info("Adding Batch .... ");
		if (dbName.equals("")) {
			log.error("No database is Identified");
			throw new SQLException("No database is Identified");
		}

		if (closed) {
			log.error("Failed to add Batch : " + "\n" + "Statement is already closed");
			throw new SQLException("Failed to add Batch  : " + "Statement is already closed");
		}

		log.info("Batch added Successfully");
		batch.add(sql);
	}

	@Override
	public void clearBatch() throws SQLException {
		// TODO Auto-generated method stub

		log.info("Clearing Batch .... ");

		if (dbName.equals("")) {
			log.error("No database is Identified");
			throw new SQLException("No database is Identified");
		}

		if (closed) {
			log.error("Failed to clear Batch :" + "\n" + "Statement is already closed");
			throw new SQLException("Failed to clear Batch :" + "Statement is already closed");
		}
		log.info("Batch is cleared Successfully");
		batch.clear();
	}

	@Override
	public void close() throws SQLException {
		// TODO Auto-generated method stub
		log.info("Executing Statement Closure.... ");
		if (dbName.equals("")) {
			log.error("No database is Identified");
			throw new SQLException("No database is Identified");
		}

		if (closed) {
			log.error("Failed to close Statemet : Statement is already closed");
			throw new SQLException("Failed to cloase Statemet " + ": Statement is already closed");
		}

		dbName = "";
		closed = true;
		batch = null;
		dbms = null;

		timeOut = 0;

		if (resultSet != null) {
			resultSet.close();
		}

		log.info("Statement is closed Successfully");

	}

	@Override
	public boolean execute(String sql) throws SQLException {
		// TODO Auto-generated method stub
		log.info("Executing SQL Query .... ");
		if (closed) {
			log.error("Failed to execute Query : Statement is closed");
			throw new SQLException("Failed to execute Query : Statement is closed");
		}

		if (dbName.equals("")) {
			log.error("No database is Identified");
			throw new SQLException("No database is Identified");
		}
		if (sql.toLowerCase().contains("create") || sql.toLowerCase().contains("drop")) {
			if (!dbms.executeStructureQuery(sql)) {
				log.error("bad query");
				return false;
			}
			return true;
		}
		if (sql.toLowerCase().contains("select")) {
			if (dbms.executeQuery(sql).length == 0) {
				log.error("bad query");

				return false;
			}
			return true;
		}

		if (sql.toLowerCase().contains("insert") || sql.toLowerCase().contains("delete")
				|| sql.toLowerCase().contains("update")) {
			if (this.executeUpdate(sql) == -2) {
				log.error("bad query");
				return false;
			}
			return true;
		}
		log.error("bad query");
		return false;
	}

	@Override
	public int[] executeBatch() throws SQLException {
		// TODO Auto-generated method stub
		log.info("Executing batch...");
		if (dbName.equals("")) {
			log.error("No database is Identified");
			throw new SQLException("No database is Identified");
		}

		if (closed) {
			log.error("Failed to execute Batch : Statement is closed");
			throw new SQLException("Failed to execute Batch : Statement is closed");
		}

		if (batch.isEmpty()) {
			log.error("Failed to execute Batch : Batch is empty");
			throw new SQLException("Failed to execute Batch : Batch is empty");
		}

		int[] result = new int[batch.size()];
		for (int i = 0; i < batch.size(); i++) {
			if (timeOut == 0) {
				try {
					
					if (this.execute(batch.get(i))) {
                        result[i] = 1;
					} else {
						result[i] = 0;
						log.error("the query number " + i + " is a bad query");
						
					}

				} catch (Exception e) {
					log.error(e.getMessage());
					throw new SQLException(e.getMessage());
				}
			} else {
				final int j = i;
				ExecutorService thread = Executors.newSingleThreadExecutor();
				Future<Boolean> future = thread.submit(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {

						Boolean result;
						try {
							result = execute(batch.get(j));
						} catch (Exception e) {
							log.error(e.getMessage());
							throw new SQLException(e.getMessage());
						}
						return result;
					}
				});

				int ans = 0;
				try {
					if (future.get(timeOut, TimeUnit.SECONDS)) {
						ans = 1;
					} else {
						log.error("the query number" + i + " is a bad query");
						
					}
				} catch (InterruptedException | ExecutionException e) {
				} catch (TimeoutException e) {
					log.error("Failed to execute update : " + "\n" + batch.get(j) + " timeout");
					throw new SQLException(batch.get(j) + "exceeded time");
				}

				thread.shutdown();
				result[i] = ans;
			}

		}

		batch.clear();
		log.info("Batch executed successfully");
		return result;
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		// TODO Auto-generated method stub
		log.info("Executing query ...");
		if (dbName.equals("")) {
			log.error("No database is Identified");
			throw new SQLException("No database is Identified");
		}

		if (closed) {
			log.error("Failed to execute query : Statement is closed");
			throw new SQLException("Failed to execute update : Statement is closed");
		}
		if (timeOut == 0) {
			try {
				selected = dbms.executeQuery(sql);
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new SQLException(e.getMessage());
			}
			if (selected == null) {
				log.info("bad query");
			} else {
				log.info("Select is successful");
			}
			return new MyResultset(this, dbms.executeQuery(sql), dbms.getmaptype(), dbms.getTableName());
		}

		ExecutorService thread = Executors.newSingleThreadExecutor();
		Future<Object[][]> future = thread.submit(new Callable<Object[][]>() {
			@Override
			public Object[][] call() throws Exception {
				try {
					selected = dbms.executeQuery(sql);
				} catch (Exception e) {
					log.error(e.getMessage());
					throw new SQLException(e.getMessage());
				}
				return selected;
			}
		});

		Object[][] result = null;

		try {
			result = future.get(timeOut, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException e) {
		} catch (TimeoutException e) {
			log.error("Failed to execute update : " + "\n" + sql + " timeout");
			throw new SQLException(sql + "exceeded time");
		}

		thread.shutdown();
		if (selected == null) {
			log.info("bad query");
		} else {
			log.info("Select is successful");
		}
		return new MyResultset(this, dbms.executeQuery(sql), dbms.getmaptype(), dbms.getTableName());

	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		// TODO Auto-generated method stub
		log.info("Executing Update query ...");
		if (dbName.equals("")) {
			log.error("No database is Identified");
			throw new SQLException("No database is Identified");
		}

		if (closed) {
			log.error("Failed to execute update : Statement is closed");
			throw new SQLException("Failed to execute update : Statement is closed");
		}

		if (timeOut == 0) {
			int result;
			try {
				result = dbms.executeUpdateQuery(sql);
			} catch (SQLException e) {
				log.error(e.getMessage());
				throw new SQLException(e.getMessage());
			}
			if (result == -2) {
				log.info("bad query");
			} else {
				log.info("Table updated successfully");
			}
			return result;
		}

		ExecutorService thread = Executors.newSingleThreadExecutor();
		Future<Integer> future = thread.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				int result;
				try {
					result = dbms.executeUpdateQuery(sql);
				} catch (Exception e) {
					log.error(e.getMessage());
					throw new SQLException(e.getMessage());
				}
				return result;
			}
		});

		int result = 0;

		try {
			try {
				result = future.get(timeOut, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (TimeoutException e) {
			log.error("Failed to execute update : " + "\n" + sql + " timeout");
			throw new SQLException(sql + "exceeded time");
		}

		thread.shutdown();
		if (result == -2) {
			log.info("bad query");
		} else {
			log.info("Table updated successfully");
		}

		return result;
	}

	@Override
	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		log.info("Getting connection .... ");
		if (dbName.equals("")) {
			log.error("No database is Identified");
			throw new SQLException("No database is Identified");
		}
		if (closed) {
			log.error("Failed to get connection : Statement is already closed");
			throw new SQLException("Failed to get connection : Statement is already closed");
		}
		if (connection == null || connection.isClosed()) {
			log.error("Failed to get connection : Connection is closed");
			throw new SQLException("Failed to get connection : Connection is closed");
		}
		log.info("Getting Connection is Successful");
		return connection;
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		// TODO Auto-generated method stub
		if (dbName.equals("")) {
			log.error("No database is Identified");
			throw new SQLException("No database is Identified");
		}

		if (closed) {
			log.error("Failed to get query timeOut : Statement is closed");
			throw new SQLException("Failed to get query timeOut : Statement is closed");
		}
		log.info("Executing Query time out");
		return timeOut;
	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		log.info("Setting query TimeOut ..... ");
		if (dbName.equals("")) {
			log.error("No database is Identified");
			throw new SQLException("No database is Identified");
		}

		if (closed) {
			log.error("Failed to set TimeOut : " + "\n" + "Statement is already closed");
			throw new SQLException("Failed to set TimeOut  : " + "Statement is already closed");
		}
		if (dbName.equals("")) {
			log.error("Failed to add Batch : No database is Identified");
		}
		log.info("TimeOut is set Successful");
		timeOut = seconds;
	}

}

