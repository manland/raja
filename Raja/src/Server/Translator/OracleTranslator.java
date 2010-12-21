package Server.Translator;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

import oracle.jdbc.driver.OracleConnection;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import Query.DeleteQuery;
import Query.InsertQuery;
import Query.Pair;
import Query.UpdateQuery;
import Server.DataBase.DataBase;
import Server.DataBase.DataBaseType;


/**
 * This is our oracle translator.
 */
public class OracleTranslator extends Translator 
{
	Connection connexion;
	Statement instruction;
	
	public OracleTranslator(DataBase dataBase, String n3File, String getMetaInfo, Vector<String> prefix) 
	{
		super(dataBase, n3File, getMetaInfo, prefix);
		
		// jdbc:oracle:thin@://localhost:8000:les_maladies
		try {
			connexion = (Connection) DriverManager.getConnection("jdbc:"+
					DataBaseType.ORACLE+
					":thin:@//"+
					dataBase.getAddress()+
					":"+dataBase.getPort()+":"+dataBase.getDatabaseName(), dataBase.getUserName(), dataBase.getPassWord());
			
			instruction = (Statement) connexion.createStatement();
		} 
		catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
	}

	/**
	 * Execute a insert query.
	 */
	public boolean insert(InsertQuery query) 
	{
		String str = "";
		str += "INSERT INTO ";
		for(String table : query.getFrom()) {
			str += table+", ";
		}
		str += "VALUES (";
		for(Pair<String, String> attribut_valeur : query.getValue()) {
			str += attribut_valeur.getSecond()+", ";
		}
		str += ");";
		
		System.out.println(str);
		
		try {
			
			ResultSet resultat = (ResultSet) instruction.executeQuery(str);
			
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return false;
	}

	/**
	 * Execute a delete query.
	 */
	public boolean delete(DeleteQuery query) 
	{
		int position_connecteur=0;
		String str = "";
		
		str += "DELETE FROM ";
		for(String table : query.getFrom()) {
			str += table+", ";
		}
		str += "WHERE ";
		for(Pair<String, String> attribut_valeur : query.getWhere()) {
			str += attribut_valeur.getFirst()+"="+attribut_valeur.getSecond();
			if(position_connecteur<=query.getConnecteur().size()) {
				str += " "+query.getConnecteur().elementAt(position_connecteur);
				position_connecteur++;
			}
		}
		str += ";";
		
		System.out.println(str);
		
		try {
			
			ResultSet resultat = (ResultSet) instruction.executeQuery(str);
			
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return false;
	}

	/**
	 * Execute a update query.
	 */
	public boolean update(UpdateQuery query) 
	{
		int position_connecteur=0;
		String str = "";
		
		str += "UPDATE ";
		for(String table : query.getFrom()) {
			str += table+", ";
		}
		
		str += "SET ";
		for(Pair<String, String> attribut_valeur : query.getSet()) {
			str += attribut_valeur.getFirst()+"="+attribut_valeur.getSecond();
			if(position_connecteur<=query.getConnecteur().size()) {
				str += " "+query.getConnecteur().elementAt(position_connecteur);
				position_connecteur++;
			}
		}
		
		str += "WHERE ";
		for(Pair<String, String> attribut_valeur : query.getWhere()) {
			str += attribut_valeur.getFirst()+"="+attribut_valeur.getSecond();
		}
		str += ";";
		
		System.out.println(str);
		
		try {
			
			ResultSet resultat = (ResultSet) instruction.executeQuery(str);
			
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return false;
	}

}
