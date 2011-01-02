package Server.Translator;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import Query.DeleteQuery;
import Query.InsertQuery;
import Query.Pair;
import Query.UpdateQuery;
import Server.DataBase.DataBase;
import Server.DataBase.DataBaseType;


/**
 * This is our MySql Translator.
 */
public class MySqlTranslator extends Translator 
{
	private Connection connexion;
	private Statement instruction;

	public MySqlTranslator(DataBase dataBase, String n3File, String getMetaInfo, Vector<Pair<String, String>> prefix) 
	{
		super(dataBase, n3File, getMetaInfo, prefix);

		try 
		{

			// ex: DriverManager.getConnection("jdbc:mysql://localhost/les_maladies","root","pass");
			connexion = (Connection) DriverManager.getConnection(
					"jdbc:"+DataBaseType.MYSQL.toString()+"://"+					
					dataBase.getAddress()+"/"+
					dataBase.getDatabaseName(),
					dataBase.getUserName(),
					dataBase.getPassWord());

			instruction = (Statement) connexion.createStatement();

			/*ResultSet resultat = (ResultSet) instruction.executeQuery("SELECT * FROM MALADIE");
			while(resultat.next()){


				System.out.println("---------------------------");
				System.out.println("RM_ID: "+resultat.getInt("RM_ID"));
				System.out.println("RM_LIBELLE: "+resultat.getString("RM_LIBELLE"));
			}*/

		}
		catch (SQLException e) 
		{
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
		for(String table : query.getFrom()) 
		{
			str += table+" ";
		}
		str += "VALUES (";
		for(int i=0; i<query.getValue().size();i++) 
		{
			str += query.getValue().get(i);
			if(i+1<query.getValue().size()){
				str+=",";
			}
		}
		str += ");";

		System.out.println(str);
		int resultat = -1;
		try 
		{
			resultat = instruction.executeUpdate(str);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		if(resultat>0){
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Execute a delete query.
	 */
	public boolean delete(DeleteQuery query) 
	{
		int position_connecteur=0;
		String str = "";

		str += "DELETE FROM ";
		for(String table : query.getFrom()) 
		{
			str += table+" ";
		}
		str += "WHERE ";
		for(int i=0; i<query.getWhere().size();i++) 
		{
			if(!query.getWhere().get(i).getFirst().equals("")){
				str += query.getWhere().get(i).getFirst()+"="+query.getWhere().get(i).getSecond();
//				if(position_connecteur<=query.getConnecteur().size()) 
//				{
//					str += " "+query.getConnecteur().elementAt(position_connecteur);
//					position_connecteur++;
//				}
			}
		}
		str += ";";

		System.out.println(str);

		int resultat = 0;
		try 
		{
			 resultat = instruction.executeUpdate(str);
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		if(resultat>0){
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Execute a update query.
	 */
	public boolean update(UpdateQuery query) 
	{
		int position_connecteur=0;
		String str = "";

		str += "UPDATE ";
		for(String table : query.getFrom()) 
		{
			str += table+", ";
		}
		str += "SET ";
		for(Pair<String, String> attribut_valeur : query.getSet()) 
		{
			str += attribut_valeur.getFirst()+"="+attribut_valeur.getSecond();
			if(position_connecteur<=query.getConnecteur().size()) 
			{
				str += " "+query.getConnecteur().elementAt(position_connecteur);
				position_connecteur++;
			}
		}
		str += "WHERE ";
		for(Pair<String, String> attribut_valeur : query.getWhere()) 
		{
			str += attribut_valeur.getFirst()+"="+attribut_valeur.getSecond();
		}
		str += ";";
		System.out.println(str);
		int resultat = -1;
		try 
		{
			resultat =  instruction.executeUpdate(str);
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		if(resultat>0)
		{
			return true;
		}
		else{
			return false;
		}
	}

	public HashMap<String, Vector<String>> getMetaInfoFromDataBase() throws SQLException{
		HashMap<String, Vector<String>> res = new HashMap<String, Vector<String>>();
		
		Vector<String> res_col = new Vector<String>();
		
		DatabaseMetaData meta_donnes = connexion.getMetaData();
	    
		String[] types = { "TABLE" };
	    ResultSet r = meta_donnes.getTables(null, null, "%", types);

		while(r.next()){
			res.put(r.getString(3), new Vector<String>());
		}
	    ResultSet rc = meta_donnes.getColumns(null, null, "%", null);
	    while(rc.next()){
	    	res.get(rc.getString(3)).add(rc.getString(4));
	    }	
		return res;
	}
	
}
