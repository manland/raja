package Server.Translator;

import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
 * This is our MySql Translator.
 */
public class MySqlTranslator extends Translator 
{
	private Connection connexion;
	private Statement instruction;

	public MySqlTranslator(DataBase dataBase, String n3File, Vector<Pair<String, String>> prefix) throws DataBaseNotAccessibleException 
	{
		super(dataBase, n3File, prefix);

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
			isConnect = true;
		}
		catch (SQLException e) 
		{
			isConnect = false;
			throw new DataBaseNotAccessibleException(database, "The database isn't connectable, modify the config.xml = "+e.getMessage());
		}
	}

	/**
	 * Execute a insert query.
	 * @throws DataBaseNotAccessibleException 
	 */
	public boolean insert(InsertQuery query) throws DataBaseNotAccessibleException 
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
			if(i+1<query.getValue().size())
			{
				str+=",";
			}
		}
		str += ");";
		int resultat = -1;
		try 
		{
			resultat = instruction.executeUpdate(str);
		}
		catch (SQLException e) 
		{
			throw new DataBaseNotAccessibleException(database, e.getMessage());
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
	 * Execute a delete query.
	 * @throws DataBaseNotAccessibleException 
	 */
	public boolean delete(DeleteQuery query) throws DataBaseNotAccessibleException 
	{
		String str = "DELETE FROM ";
		for(String table : query.getFrom()) 
		{
			str += table+" ";
		}
		str += "WHERE ";
		for(int i=0; i<query.getWhere().size();i++) 
		{
			if(!query.getWhere().get(i).getFirst().equals(""))
			{
				str += query.getWhere().get(i).getFirst()+"="+query.getWhere().get(i).getSecond();
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
			throw new DataBaseNotAccessibleException(database, e.getMessage());
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
	 * @throws DataBaseNotAccessibleException 
	 */
	public boolean update(UpdateQuery query) throws DataBaseNotAccessibleException 
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
			throw new DataBaseNotAccessibleException(database, e.getMessage());
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

	public HashMap<String, Vector<String>> getMetaInfoFromDataBase() throws DataBaseNotAccessibleException
	{
		HashMap<String, Vector<String>> res = new HashMap<String, Vector<String>>();
		DatabaseMetaData meta_donnes;
		try {
			meta_donnes = connexion.getMetaData();
			String[] types = { "TABLE" };
		    ResultSet r = meta_donnes.getTables(null, null, "%", types);
			while(r.next())
			{
				res.put(r.getString(3), new Vector<String>());
			}
		    ResultSet rc = meta_donnes.getColumns(null, null, "%", null);
		    while(rc.next())
		    {
		    	res.get(rc.getString(3)).add(rc.getString(4));
		    }	
		} 
		catch (SQLException e) 
		{
			throw new DataBaseNotAccessibleException(database, "The database don't accept getMetaData()");
		}
		catch (Exception ee)
		{
			throw new DataBaseNotAccessibleException(database, "The database isn't connectable, modify the config.xml");
		}
		return res;
	}
	
}
