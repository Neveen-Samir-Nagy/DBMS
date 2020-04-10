package eg.edu.alexu.csd.oop.jdbc.cs54;

public interface Iterator {
	public boolean next();
	public boolean previous();
	public boolean last();
	public boolean first();
	public boolean isLast();
	public boolean isFirst();
	public boolean isAfterLast();
	public boolean isBeforeFirst();
	public boolean absolute(int row);
	public void afterLast();
	public void beforeFirst();
	public Object getObject(int col);
	public Object [][] getSelected();
}
