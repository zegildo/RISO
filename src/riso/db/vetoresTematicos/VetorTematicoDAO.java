package riso.db.vetoresTematicos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jena.VetorTematico;

public class VetorTematicoDAO {


	public List<VetorTematico> obtemVetoresTematicos(String conceito){
		Connection con = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		List<VetorTematico> vetores = new ArrayList<VetorTematico>();  
		try {
			con = DBConexion.getInstance().getConnection();
			stm = con.prepareStatement("SELECT vetoresTematicos FROM vectors where conceito = "+conceito);
			rs = stm.executeQuery();  
			while(rs.next()){  
				VetorTematico vetor = new VetorTematico(rs.getString("vetoresTematicos"),conceito);  
				vetores.add(vetor);  
			}  
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		} finally {
			DBConexion.closeResult(rs);  
			DBConexion.closeStatement(stm);  
			DBConexion.closeConnection(con);
		}
		
		return vetores;  
	}  

	

	public void salvarVetores(List<VetorTematico> vetoresTematicos){  
		PreparedStatement stm = null;
		try{
			stm = (PreparedStatement) DBConexion.getInstance().getConnection().createStatement();
			Iterator<VetorTematico> it = vetoresTematicos.iterator();
			
			while(it.hasNext()){  
				VetorTematico vetorDaVez = it.next();
				String conceito = vetorDaVez.getConceito();
				String vetor = vetorDaVez.toString();
				String query = "INSERT INTO vectors VALUES ('"+conceito+"','"+vetor+"')";  
				stm.addBatch(query); 
			}  
			stm.executeBatch(); 

		}catch(SQLException e){
		  e.printStackTrace();
		}finally{
			DBConexion.closeStatement(stm);  
		}
	}   

}
