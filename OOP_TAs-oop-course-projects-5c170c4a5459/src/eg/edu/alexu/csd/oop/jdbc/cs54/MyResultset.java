package eg.edu.alexu.csd.oop.jdbc.cs54;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyResultset extends AbsractResultset{
	private MyStatement statement;
	private boolean close;
	private SelectedArr sel;
	private Iterator iter; 
	private String tableName;
	private Map<String,String> maptype;
	MyResultset(MyStatement statement, Object[][] selected ,Map<String,String> maptype,String tableName){
		this.statement = statement;
		this.maptype = maptype;
		this.sel = new SelectedArr(selected);
		iter = sel.getIterator();
		this.tableName = tableName;
		this.close = false;
	}
	
	@Override
	public boolean absolute(int row) throws SQLException {
		// TODO Auto-generated method stub
		return iter.absolute(row);
	}

	@Override
	public void afterLast() throws SQLException {
		// TODO Auto-generated method stub
			iter.afterLast();
	}

	@Override
	public void beforeFirst() throws SQLException {
		// TODO Auto-generated method stub
			iter.beforeFirst();
	}
	//When a Statement object is closed, its current ResultSet object, if one exists, is also closed
	@Override
	public void close() throws SQLException {
		close = true;
		statement = null;
	}

	@Override
	public int findColumn(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		int index = 0;
		if(maptype.size()==0) {
			throw new SQLException();
		}else {
			ArrayList<String> indexes = new ArrayList<String>(maptype.keySet());
			index = indexes.indexOf(arg0);
		}
		return index;
	}

	@Override
	public boolean first() throws SQLException {
		// TODO Auto-generated method stub
		
		return iter.first();
	}

	@Override
	public int getInt(int col) throws SQLException {
		// TODO Auto-generated method stub
		return (int) iter.getObject(col);
	}

	@Override
	public int getInt(String col) throws SQLException {
		// TODO Auto-generated method stub
		List<String> indexes = new ArrayList<String>(maptype.keySet());
		return (int) iter.getObject(indexes.indexOf(col) + 1);
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		// TODO Auto-generated method stub
		return new MyResultSetMetaData(iter.getSelected(),maptype,tableName);
	}

	@Override
	public Object getObject(int col) throws SQLException {
		// TODO Auto-generated method stub
		return iter.getObject(col);
	}

	@Override
	public Statement getStatement() throws SQLException {
		// TODO Auto-generated method stub
		return statement;
	}

	@Override
	public String getString(int col) throws SQLException {
		// TODO Auto-generated method stub
		return (String) iter.getObject(col);
	}
	
	@Override
	public String getString(String col) throws SQLException {
		// TODO Auto-generated method stub
		List<String> indexes = new ArrayList<String>(maptype.keySet());
		return (String) iter.getObject(indexes.indexOf(col) + 1);
	}

	@Override
	public boolean isAfterLast() throws SQLException {
		// TODO Auto-generated method stub
		
		return iter.isAfterLast();
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		// TODO Auto-generated method stub
	
		return iter.isBeforeFirst();
	}

	@Override
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		//merna
		return close;
	}

	@Override
	public boolean isFirst() throws SQLException {
		// TODO Auto-generated method stub
		
		return iter.isFirst();
	}

	@Override
	public boolean isLast() throws SQLException {
		// TODO Auto-generated method stub
	
		return iter.isLast();
	}

	@Override
	public boolean last() throws SQLException {
		// TODO Auto-generated method stub
		
		return iter.last();
	}

	@Override
	public boolean next() throws SQLException {
		// TODO Auto-generated method stub
		return iter.next();
	}

	@Override
	public boolean previous() throws SQLException {
		// TODO Auto-generated method stub
		return iter.previous();
	}

}
