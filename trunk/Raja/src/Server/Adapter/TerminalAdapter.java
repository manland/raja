package Server.Adapter;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import Query.SelectQuery;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import de.fuberlin.wiwiss.d2rq.ModelD2RQ;
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
	/**
	 * Translator used to convert queries to specified database.
	 */
	protected ITranslator translator;
	private Model model;
	
	
	public TerminalAdapter(ITranslator translator) 
	{
		this.translator = translator;
		model = ModelFactory.createDefaultModel();
	}


	/**
	 * Return the RDF schema of the databas linked by the adapter.
	 * @throws DataBaseNotAccessibleException 
	 */
	public ResultSet getLocalSchema() throws DataBaseNotAccessibleException 
	{
		return translator.getMetaInfo();
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
