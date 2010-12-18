package Server.Adapter;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;

import Exception.DataBaseNotAccessibleException;
import Query.IQuery;
import Server.Factory;
import Server.DataBase.DataBase;
import Server.DataBase.DataBaseType;
import Server.Translator.ITranslator;
import Server.Translator.MySqlTranslator;
import Server.Translator.OracleTranslator;
import Server.Translator.PostGreTranslator;

import com.hp.hpl.jena.query.ResultSet;

/**
 * Adapter directly linked to a database.
 */
public class TerminalAdapter extends Adapter 
{
	
	public TerminalAdapter(Vector<String> prefix, ITranslator translator) 
	{
		super(prefix);
	}

	/**
	 * Translator used to convert queries to specified database.
	 */
	protected ITranslator translator;

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
