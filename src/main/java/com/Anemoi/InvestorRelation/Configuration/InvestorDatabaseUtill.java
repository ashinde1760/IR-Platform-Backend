package com.Anemoi.InvestorRelation.Configuration;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
 
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class InvestorDatabaseUtill {
 
	private static final Logger logger = LoggerFactory.getLogger(InvestorDatabaseUtill.class);
	private static final String URL_TABLE = "url_table";
	private static final String URL_DB = "url_db";
	private static final String USER = "user";
	private static final String PASSWORD = "password";
	private static final String DRIVER = "driver";
	private static BasicDataSource basicdatasource = null;
 
	public static Connection getForDatabaseConnection() throws ClassNotFoundException {
		Connection connection = null;
 
		try {
 
//			String username = System.getenv("DB_USERNAME");
//			String password = System.getenv("DB_PASSWORD");
//			String host = System.getenv("DB_HOST");
//			String url = "jdbc:sqlserver://localhost";
//			System.out.println(url+ username+ password);
//			connection = DriverManager.getConnection(url, username, password);
 
			basicdatasource = new BasicDataSource();
			Properties properties = ReadProperties.getPropertiesFromResources("db.properties");
//			System.out.println("check driver " + properties.getProperty(DRIVER));
			basicdatasource.setDriverClassName(properties.getProperty(DRIVER));
//			System.out.println("check url " + properties.getProperty(URL_DB));
			basicdatasource.setUrl(properties.getProperty(URL_DB));
//			System.out.println("check user " + properties.getProperty(USER));
			basicdatasource.setUsername(properties.getProperty(USER));
//			System.out.println("check password " + properties.getProperty(PASSWORD));
			basicdatasource.setPassword(properties.getProperty(PASSWORD));
			connection = basicdatasource.getConnection();
 
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println("unable to connect database");
			e.printStackTrace();
		}
		if (connection != null) {
			return connection;
 
		}
		return null;
 
	}
 
	// this method use when work on local
 
	public static Connection getConnection() throws ClassNotFoundException {
		Connection connection = null;
 
		try {
 
//			String username = System.getenv("DB_USERNAME");
//			String password = System.getenv("DB_PASSWORD");
//			String host = System.getenv("DB_HOST");
//			String url = "jdbc:sqlserver://localhost";		
//			System.out.println(url+ username+ password);
//			connection = DriverManager.getConnection(url, username, password);
 
			basicdatasource = new BasicDataSource();
			Properties properties = ReadProperties.getPropertiesFromResources("db.properties");
//			System.out.println("check driver " + properties.getProperty(DRIVER));
			basicdatasource.setDriverClassName(properties.getProperty(DRIVER));
//			System.out.println("check driver " + properties.getProperty(DRIVER));
			basicdatasource.setUrl(properties.getProperty(URL_TABLE));
//			System.out.println("check user " + properties.getProperty(USER));
			basicdatasource.setUsername(properties.getProperty(USER));
//			System.out.println("check password " + properties.getProperty(PASSWORD));
			basicdatasource.setPassword(properties.getProperty(PASSWORD));
			connection = basicdatasource.getConnection();
 
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println("unable to connect database");
			e.printStackTrace();
		}
		if (connection != null) {
			return connection;
 
		}
		return null;
 
	}
 
	public static void close(Statement statement, Connection connection) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
	}
 
	public static void close(ResultSet resultset, PreparedStatement statment, Connection connectionclose) {
		if (resultset != null) {
			try {
				resultset.close();
			} catch (SQLException e) {
 
				// TODO: handle exception
			}
		}
		if (statment != null) {
			try {
				statment.close();
			} catch (SQLException e) {
 
				// TODO: handle exception
			}
		}
		if (connectionclose != null) {
			try {
				connectionclose.close();
			} catch (SQLException e) {
 
				// TODO: handle exception
			}
 
		}
	}
 
}