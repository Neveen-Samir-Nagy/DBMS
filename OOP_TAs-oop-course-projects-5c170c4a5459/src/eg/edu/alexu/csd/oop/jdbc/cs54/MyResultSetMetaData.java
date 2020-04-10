package eg.edu.alexu.csd.oop.jdbc.cs54;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyResultSetMetaData extends AbsractResultSetMetaData{

	private Object[][] selected;
	private String tableName="";
	private Map<String,String> maptype = new HashMap<String,String>();
	MyResultSetMetaData(Object[][] sel, Map<String,String> type,String table) {
		// TODO Auto-generated constructor stub
		selected = new Object[sel.length][sel[0].length];
		selected = sel;
		maptype = type;
		tableName = table;
	}
	@Override
	public int getColumnCount() throws SQLException {
		// TODO Auto-generated method stub
		return selected[0].length;
	}

	@Override
	public String getColumnLabel(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		if(arg0<=0 || arg0>selected[0].length) {
			return null;
			}else {
				ArrayList<String> indexes = new ArrayList<String>(maptype.keySet());
				return indexes.get(arg0-1);
			}
	}

	@Override
	public String getColumnName(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		if(arg0<=0 || arg0>selected[0].length) {
		return null;
		}else {
			ArrayList<String> indexes = new ArrayList<String>(maptype.keySet());
			return indexes.get(arg0-1);
		}
	}

	@Override
	public int getColumnType(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		if(arg0<=0 || arg0>selected[0].length) {
			return 0;
			}else {
				ArrayList<String> indexes = new ArrayList<String>(maptype.keySet());
				String coulmn = indexes.get(arg0-1);
				if(maptype.get(coulmn).toLowerCase().equals("varchar")) {
					return java.sql.Types.LONGNVARCHAR;
				}else {
					return java.sql.Types.INTEGER;
				}
			}
	}

	@Override
	public String getTableName(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		if(arg0<=0 || arg0>selected[0].length) {
			return "";
		}
		return tableName;
	}

}
