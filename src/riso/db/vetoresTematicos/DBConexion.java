package riso.db.vetoresTematicos;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBConexion {

	private final static DBConexion instance = new DBConexion();
	private ComboPooledDataSource cpds;

	private DBConexion() {
		try {
			cpds = new ComboPooledDataSource();
			cpds.setDriverClass("org.postgresql.Driver");
			cpds.setJdbcUrl("jdbc:postgresql://localhost:23456/jena");
			cpds.setUser("postgres");
			cpds.setPassword("postgres");
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeConnection(Connection con) {
		if(con != null){
			try {
				con.close();
			} catch (SQLException e) {
			}
		}
	}
	
	public static void closeResult(ResultSet rs) {
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
	}
	
	public static void closeStatement(PreparedStatement stm) {
		if(stm != null){
			try {
				stm.close();
			} catch (SQLException e) {
			}
		}
	}

	public Connection getConnection() throws SQLException {
		return cpds.getConnection();
	}

	public static DBConexion getInstance() {
		return instance;
	}


}
