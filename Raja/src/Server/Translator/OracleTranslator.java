package Server.Translator;
import java.util.Vector;

import Query.DeleteQuery;
import Query.InsertQuery;
import Query.UpdateQuery;
import Server.DataBase.DataBase;


/**
 * This is our oracle translator.
 */
public class OracleTranslator extends Translator 
{
	
	public OracleTranslator(DataBase dataBase, String n3File, String getMetaInfo, Vector<String> prefix) 
	{
		super(dataBase, n3File, getMetaInfo, prefix);
	}

	/**
	 * Execute a insert query.
	 */
	public boolean insert(InsertQuery query) 
	{
		return false;
	}

	/**
	 * Execute a delete query.
	 */
	public boolean delete(DeleteQuery query) 
	{
		return false;
	}

	/**
	 * Execute a update query.
	 */
	public boolean update(UpdateQuery query) 
	{
		return false;
	}

}
