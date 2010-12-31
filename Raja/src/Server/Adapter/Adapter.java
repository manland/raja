package Server.Adapter;

import java.util.Vector;

import Exception.DataBaseNotAccessibleException;
import Query.IQuery;
import Query.Pair;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.rdf.model.Model;

public abstract class Adapter implements IAdapter {
	
	private Vector<Pair<String, String>> prefix;
	
	public Adapter(Vector<Pair<String, String>> prefix)
	{
		this.prefix = new Vector<Pair<String, String>>();
		this.prefix.addAll(prefix);
	}
	
	public Vector<Pair<String, String>> getPrefix()
	{
		return prefix;
	}
	
	@Override
	public abstract Model execute(IQuery query) throws DataBaseNotAccessibleException;

	@Override
	public abstract Model getLocalSchema() throws DataBaseNotAccessibleException;

	protected Model execQueryDescribe(String query, Model model) 
	{		
		Model result_model = null;
		Query q = null;
		QueryExecution qexec = null;
		try
		{
			q = QueryFactory.create(query) ;
			qexec = QueryExecutionFactory.create(q,model) ;
			result_model = qexec.execDescribe() ;
			qexec.close();
		}
		catch (QueryParseException e)
		{
			System.err.println(e.getMessage());
		}
		return result_model;
	}
	
}
