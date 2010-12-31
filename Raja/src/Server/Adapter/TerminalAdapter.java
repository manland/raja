package Server.Adapter;
import java.util.Vector;


import Exception.DataBaseNotAccessibleException;
import Query.IQuery;
import Query.Pair;
import Query.SelectQuery;
import Server.Translator.ITranslator;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * Adapter directly linked to a database.
 */
public class TerminalAdapter extends Adapter 
{
	/**
	 * Translator used to convert queries to specified database.
	 */
	protected ITranslator translator;

	public TerminalAdapter(Vector<Pair<String, String>> prefix, ITranslator translator) 
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
		Model result_model = ModelFactory.createDefaultModel();
		for(int i=0; i<getPrefix().size(); i++)
		{
			result_model.setNsPrefix(getPrefix().get(i).getFirst(), getPrefix().get(i).getSecond());
		}
		Query q = null;

		try
		{
			if(query.getClass().getSimpleName().equals("SelectQuery"))
			{
				SelectQuery sq = (SelectQuery)query;
				Model schema_local = getLocalSchema();
				for(int i=0; i<sq.getWhere().size();i++)
				{
					Model res_d = execQueryDescribe(SelectQuery.createDescribeQuery(getPrefix(), sq.getWhere().get(i), SelectQuery.DROITE).getQuery(), schema_local);
					if(res_d!=null)
					{
						result_model.add(translator.exec(SelectQuery.createSimpleSelectQuery(getPrefix(), sq.getWhere().get(i), SelectQuery.DROITE)));
					}	
					else
					{
						Model res_g = execQueryDescribe(SelectQuery.createDescribeQuery(getPrefix(), sq.getWhere().get(i), SelectQuery.GAUCHE).getQuery(), schema_local);
						if(res_g!=null)
						{
							result_model.add(translator.exec(SelectQuery.createSimpleSelectQuery(getPrefix(), sq.getWhere().get(i), SelectQuery.GAUCHE)));
						}
						else
						{
							Model res_m = execQueryDescribe(SelectQuery.createDescribeQuery(getPrefix(), sq.getWhere().get(i), SelectQuery.MILIEU).getQuery(), schema_local);
							if(res_m!=null)
							{
								result_model.add(translator.exec(SelectQuery.createSimpleSelectQuery(getPrefix(), sq.getWhere().get(i), SelectQuery.MILIEU)));
							}
						}
					}
				}
			}
		}
		catch (QueryParseException e)
		{
			System.err.println(e.getMessage() + " :: prefix=" + getPrefix());
		}
		return result_model;
	}
}
