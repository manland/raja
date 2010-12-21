package Server.Adapter;
import java.util.Vector;


import Exception.DataBaseNotAccessibleException;
import Query.IQuery;
import Query.SelectQuery;
import Server.Translator.ITranslator;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

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
		this.translator = translator;
	}

	/**
	 * Return the RDF schema of the databas linked by the adapter.
	 * @throws DataBaseNotAccessibleException 
	 */
	public Model getLocalSchema() throws DataBaseNotAccessibleException 
	{
		return translator.getMetaInfo();
	}


	/**
	 * Execute the query. 
	 */
	public Model execute(IQuery query) throws DataBaseNotAccessibleException 
	{
		Model result_model = null;
		Query q = null;
		try
		{
			q = QueryFactory.create(SelectQuery.selectQueryToDescribeQuery(getPrefix(), (SelectQuery)query).getQuery()) ;
			QueryExecution qexec = QueryExecutionFactory.create(q,getLocalSchema()) ;
			result_model = qexec.execDescribe() ;
		}
		catch (QueryParseException e){
			System.err.println(e.getMessage());
			System.out.println(getPrefix());
		}
		if(result_model!=null)
		{
			return translator.exec(query);
		}
		else
		{
			return null;
		}
	}


}
