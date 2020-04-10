package eg.edu.alexu.csd.oop.db.cs54;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class CreateANDDrop {
	public String createdatabase(String namedatabase) {
		File database = new File( namedatabase);
		database.mkdirs();
		return database.getAbsolutePath();
	}
	public void createtable(String Folder,String nametable) {
		
		File tabledtd = new File(Folder + System.getProperty("file.separator") + nametable+".dtd");
		if(tabledtd.exists()) {
			try {
				throw new SQLException();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			tabledtd.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File tabledtdtype = new File(Folder + System.getProperty("file.separator") + nametable+"dtd.xml");
		try {
			tabledtdtype.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String file_name = Folder + System.getProperty("file.separator") + nametable+".xml";
		File table = new File(file_name);
		
		try {
			table.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String dropdatabase(String pathdatabase) {
		File database = new File(pathdatabase);
		if (!database.exists()) {
			createdatabase(pathdatabase);
		}
		for (File file: database.listFiles()) {
	        if (!file.isDirectory()) {
	            file.delete();
	        }
	    }
		return database.getAbsolutePath();
	}
	public void droptable(String Folder,String nametable) {
		File table = new File(Folder + System.getProperty("file.separator") + nametable+".xml");
		table.delete();
		File tabledtd = new File(Folder + System.getProperty("file.separator") + nametable+".dtd");
		tabledtd.delete();
		File tabledtd2 = new File(Folder + System.getProperty("file.separator") + nametable+"dtd.xml");
		tabledtd2.delete();
	}
}
