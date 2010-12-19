package Server.Translator;
import java.util.Vector;

import Server.DataBase.DataBase;
import Server.Translator.N3.IN3Translator;
import Server.Translator.N3.N3Translator;
import Exception.DataBaseNotAccessibleException;
import Query.DeleteQuery;
import Query.IQuery;
import Query.InsertQuery;
import Query.SelectQuery;
import Query.UpdateQuery;

import com.hp.hpl.jena.query.ResultSet;


/**
 * Abstract class representing a query translator.
 */
public abstract class Translator implements ITranslator 
{
	protected IN3Translator selectTranslator;
	protected DataBase database;
	
	public Translator(DataBase dataBase, String n3File, String getMetaInfo, Vector<String> prefix) 
	{
		selectTranslator = new N3Translator(n3File, getMetaInfo, prefix);
		System.out.println("Translator::constructor::N3file="+n3File+"::dataBaseType="+dataBase.getType());
	}

	/**
	 * Execute the given query. If rs = null statement isn't execute.
	 */
	public ResultSet exec(IQuery query) throws DataBaseNotAccessibleException 
	{
		ResultSet rs = null;
		if(query.getClass().getSimpleName().equals("SelectQuery"))
		{
			rs = select((SelectQuery)query);
		}
		else if(query.getClass().getSimpleName().equals("InsertQuery"))
		{
			if(insert((InsertQuery)query))
			{
				rs = null;//??????????????????????
			}
		}
		else if(query.getClass().getSimpleName().equals("UpdateQuery"))
		{
			if(update((UpdateQuery)query))
			{
				rs = null;//??????????????????????
			}
		}
		else if(query.getClass().getSimpleName().equals("DeleteQuery"))
		{
			if(delete((DeleteQuery)query))
			{
				return null;//??????????????????????
			}
		}
		return rs;
	}

	/**
	 * Execute a select query.
	 */
	public ResultSet select(SelectQuery query) throws DataBaseNotAccessibleException 
	{
		return selectTranslator.select(query);
	}

	public ResultSet getMetaInfo() throws DataBaseNotAccessibleException 
	{
		return selectTranslator.getMetaInfo();
	}

}
