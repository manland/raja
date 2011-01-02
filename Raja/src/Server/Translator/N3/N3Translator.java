package Server.Translator.N3;

import java.util.Vector;

import Query.Pair;
import Query.SelectQuery;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.rdf.model.Model;

import de.fuberlin.wiwiss.d2rq.ModelD2RQ;

public class N3Translator implements IN3Translator 
{
	
	private Vector<Pair<String, String>> prefix;
	private Model model;
	
	public N3Translator(String n3File, Vector<Pair<String, String>> prefix)
	{
		this.prefix = prefix;
		model = new ModelD2RQ(n3File);
	}
	
	public Model select(SelectQuery query) 
	{
		Model result_model = null;
		Query q = null;
		try
		{
			q = QueryFactory.create(SelectQuery.selectQueryToDescribeQuery(prefix, query).getQuery());
			QueryExecution qexec = QueryExecutionFactory.create(q,model) ;
			result_model = qexec.execDescribe() ;
		}
		catch (QueryParseException e)
		{
			System.err.println(e.getMessage());
		}
		return result_model;
	}
}
