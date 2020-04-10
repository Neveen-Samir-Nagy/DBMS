package eg.edu.alexu.csd.oop.db.cs54;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		int i = 1; 
		int flag = 0;
		Data db = new Data();
		while(i == 1) {
			
			
			Scanner sc = new Scanner(System.in);
			
			System.out.println("enter query:");
				String query = sc.nextLine();
				
				if(query.toLowerCase().contains("drop") ||query.toLowerCase().contains("create") ) {
					
					if(query.toLowerCase().contains("create") && query.toLowerCase().contains("database")) {
						flag =1;
						
					}
					try {
						boolean done = false;
						done = db.executeStructureQuery(query);
						if(!done) {
							System.out.println("BAD Query");
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(query.toLowerCase().contains("select")) {
					 if(flag == 0) {
						 System.out.println("you must open a database first!");
					 }
					 else {
					try {
						
						Object[][] select = db.executeQuery(query);
						 if(select == null) {
							 System.out.println("bad query");
						 }
						 else {
							 System.out.println(Arrays.deepToString(select));
						 }
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 }
				}
				else if(query.toLowerCase().contains("delete") || query.toLowerCase().contains("insert") || query.toLowerCase().contains("update")) {
					 if(flag == 0) {
						 System.out.println("you must open a database first!");
					 }
					 else {
					try {
						//db.executeUpdateQuery(query);
						if(db.executeUpdateQuery(query) == -1) {
							System.out.println("bad query");
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				}
			else {
				System.out.println("bad query");
			}
		}
	}
}
