package Server.Adapter;
import java.util.Vector;


import Exception.DataBaseNotAccessibleException;
import Query.IQuery;
import Server.Translator.ITranslator;

import com.hp.hpl.jena.query.ResultSet;

/**
 * Adapter directly linked to a database.
 */
public class TerminalAdapter extends Adapter 
{
	/**
	 * Translator used to convert queries to specified database.
	 */
	protected ITranslator translator;
	
	public TerminalAdapter(Vector<String> prefix, ITranslator translator) 
	{
		super(prefix);
		System.out.println("TerminalAdapter::constructor");
	}

	/**
	 * Return the RDF schema of the databas linked by the adapter.
	 */
	public ResultSet getLocalSchema() 
	{
		return null;
	}

	/**
	 * Return true if query match to database
	 */
	public boolean isQueryMatching(IQuery query) 
	{
		return true;
	}

	/**
	 * Execute the query. 
	 */
	public ResultSet execute(IQuery query) throws DataBaseNotAccessibleException 
	{
		return null;
	}
}
