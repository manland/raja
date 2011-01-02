package Server.Translator;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import Exception.DataBaseNotAccessibleException;
import Query.DeleteQuery;
import Query.InsertQuery;
import Query.Pair;
import Query.UpdateQuery;
import Server.DataBase.DataBase;
import Server.DataBase.DataBaseType;

/**
 * This is our PostGre Translator.
 */
public class PostGreTranslator extends Translator 
{
	Connection connexion;
	Statement instruction;
	
	public PostGreTranslator(DataBase dataBase, String n3File, Vector<Pair<String, String>> prefix) throws DataBaseNotAccessibleException 
	{
		super(dataBase, n3File, prefix);
		
		// jdbc:postgresql://host:port/database
		try 
		{
			connexion = (Connection) DriverManager.getConnection(
					"jdbc:"+DataBaseType.POSTGRE.toString()+"://"+					
					dataBase.getAddress()+":"+
					dataBase.getPort()+"/"+
					dataBase.getDatabaseName(),
					dataBase.getUserName(),
					dataBase.getPassWord());
			
			instruction = (Statement) connexion.createStatement();
		} 
		catch (SQLException e) 
		{
			throw new DataBaseNotAccessibleException(database, "The database isn't connectable, modify the config.xml");
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
			str += table+", ";
		}
		str += "WHERE ";
		for(Pair<String, String> attribut_valeur : query.getWhere()) 
		{
			str += attribut_valeur.getFirst()+"<>"+attribut_valeur.getSecond();
			if(position_connecteur<=query.getConnecteur().size()) 
			{
				str += " "+query.getConnecteur().elementAt(position_connecteur);
				position_connecteur++;
			}
		}
		str += ";";
		System.out.println(str);
		int resultat = -1;
		try 
		{
			resultat = instruction.executeUpdate(str);
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		if(resultat>0)
		{
			return true;
		}
		else 
		{
			return false;
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
			str += table+", ";
		}
		str += "VALUES (";
		for(int i=0; i<query.getValue().size();i++) 
		{
			str += query.getValue().get(i);
			if(i+1<query.getValue().size())
			{
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
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		if(resultat>0)
		{
			return true;
		}
		else 
		{
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
			resultat = instruction.executeUpdate(str);
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		if(resultat>0)
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	public HashMap<String,Vector<String>> getMetaInfoFromDataBase() throws DataBaseNotAccessibleException
	{
		throw new DataBaseNotAccessibleException(database, "The database don't accept getMetaData()");
		//return null;
	}
}
