package eg.edu.alexu.csd.oop.db.cs54;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DTD {
	
public void writeDTD(String file_name , String table_name , ArrayList<String> col_name )
		throws IOException {
	
		FileWriter fw = null;
		BufferedWriter w =null;
		File file = new File(file_name);
		file.createNewFile();
		String col = "";
		
		try {
		fw =new FileWriter(file);
		w =new BufferedWriter(fw);
		
		w.write("<!ELEMENT " + table_name + " (Row*)>" + System.lineSeparator());
		for (int i = 0; i < col_name.size(); i++) {
			if (i == col_name.size() - 1 ) {
				col += col_name.get(i);
			}
			else {
				col += col_name.get(i) + ",";
			}
		}
		w.write("<!ELEMENT " + "Row " + "(" + col + ")>" + System.lineSeparator());
		for (int i = 0; i < col_name.size(); i++) {
			w.write("<!ELEMENT " + col_name.get(i) + " (#PCDATA)>" + System.lineSeparator());
		}
		w.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
		if(fw!=null){
			fw.close();
		}
		if(w!=null){
			w.close();
		}
			} catch (IOException e) {
		e.printStackTrace();
			}
		}
	}

}
