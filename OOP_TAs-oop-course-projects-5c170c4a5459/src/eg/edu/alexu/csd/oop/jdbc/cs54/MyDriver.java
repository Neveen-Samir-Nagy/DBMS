package eg.edu.alexu.csd.oop.jdbc.cs54;

import java.io.File;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class MyDriver extends AbsractDriver{
    private MyConnection connection;
    final static Logger log = Logger.getLogger(MyDriver.class.getName());
  
	@Override
	public boolean acceptsURL(String url) throws SQLException {
		// TODO Auto-generated method stub
		log.info("Checking URL for validity....");
		if(url.equals("jdbc:xmldb://localhost")) {
			log.info("URL is valid");
			return true;
		}
		log.error("Failed to accept URL : URL is incorrect");
		return false;
	}

	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		// TODO Auto-generated method stub

		log.info("Connecting to database .... ");
		File dir = (File) info.get("path");
	    String path = dir.getAbsolutePath();

	    log.info("Connection is successful");
return new MyConnection(path);
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
		// TODO Auto-generated method stub
		log.info("Getting Driver property info ... ");
		 Driver d = DriverManager.getDriver(url);

		    if (d != null) {
		    	log.info("Porperty info is sent successfully");
		    	return d.getPropertyInfo(url, info);
		    } else {
		    	log.error("Porperty info failed to send");
				return null;
	}
		
		
	}

}
