package br.com.vinicius.dal;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionModule {

	public static Connection conector() {
		
		java.sql.Connection conection =null;
		String driver = "com.mysql.jdbc.Driver";
		
		String url = "jdbc:mysql://127.0.0.1:3306/contatos?useSSL=false";
		
		String user = "root";
		
		String password = "";

		try {
			Class.forName(driver);
			conection = DriverManager.getConnection(url,user,password);
			return conection;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
}