package eg.edu.alexu.csd.oop.jdbc.cs54;
import static org.junit.Assert.fail;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;

import eg.edu.alexu.csd.oop.TestRunner;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

public class Main {
	 static Logger logger = Logger.getLogger(Main.class);
	public static void main(String[] args) throws SQLException {
		BasicConfigurator.configure();
		MyDriver driver = new MyDriver();
		Properties info = new Properties();
		
		Pattern p;
		Matcher m;
		String databaseName = "";
		MyStatement statement = null;
		int i = 1;
		boolean flag = false;

		Scanner sc = new Scanner(System.in);
		System.out.println("enter URL:");
		String url = sc.nextLine();
		while (!driver.acceptsURL(url)) {
			System.out.println("enter URL:");
			url = sc.nextLine();
		}
		MyConnection connection = new MyConnection("");
		while (!flag) {
			System.out.println("you should open\\create database first");
			String regex = "(?i)CREATE(?-i)\\s+(?i)DATABASE(?-i)\\s+([a-zA-Z0-9_]+)";
			sc = new Scanner(System.in);
			String query = sc.nextLine();
			p = Pattern.compile(regex);
			m = p.matcher(query);
			if (m.find()) {
				flag = true;
				databaseName = m.group(1);
				connection = new MyConnection(databaseName);
				File dbDir = new File(databaseName);
				info.put("path", dbDir.getAbsoluteFile());
				connection = (MyConnection) driver.connect(url, info);
				statement = (MyStatement) connection.createStatement();
			}
			}
		while (i == 1) {
			System.out.println("1)enter query:");
			System.out.println("2)add batch");
			System.out.println("3)excute batch");
			String n = sc.nextLine();
			switch(n) {
			case "1":
				String regex = "(?i)CREATE(?-i)\\s+(?i)DATABASE(?-i)\\s+([a-zA-Z0-9_]*)";
				sc = new Scanner(System.in);
				String query = sc.nextLine();
				p = Pattern.compile(regex);
				m = p.matcher(query);
				if (m.find()) {
					flag = true;
					databaseName = m.group(1);
					connection = new MyConnection(databaseName);
					File dbDir = new File(databaseName);
					info.put("path", dbDir.getAbsoluteFile());
					connection = (MyConnection) driver.connect(url, info);
					statement = (MyStatement) connection.createStatement();
				}
				if(flag) {
					if(query.toLowerCase().contains("select")) {
						MyResultset result = (MyResultset) statement.executeQuery(query);
						MyResultSetMetaData resultset = (MyResultSetMetaData) result.getMetaData();
						result.beforeFirst();
						while(result.next()) {
							for(int j=1; j<=resultset.getColumnCount();j++) {
								System.out.print(result.getObject(j)+" ");
							}
							if(!result.isLast()) {
							System.out.println();
							}
						}
					} else {
				if (!statement.execute(query)) {
					//System.out.println("Bad Query!");
				}else {
					
					}
					}
				} else {
					System.out.println("you must create database!");
				}

				break;
			case "2":
				System.out.println("enter query:");
				sc = new Scanner(System.in);
			     query = sc.nextLine();
				statement.addBatch(query);
				break;
			case "3":
				statement.executeBatch();
				break;
			}
			
		}
	}
}
