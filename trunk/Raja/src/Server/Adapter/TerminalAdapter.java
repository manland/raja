package Server.Adapter;

import java.util.Vector;

import Exception.DataBaseNotAccessibleException;
import Query.IQuery;
import Query.Pair;
import Query.SelectQuery;
import Server.Translator.ITranslator;

import com.hp.hpl.jena.query.QueryParseException;
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
	private Model localSchema;

	public TerminalAdapter(Vector<Pair<String, String>> prefix, ITranslator translator) throws DataBaseNotAccessibleException 
	{
		super(prefix);
		this.translator = translator;
		localSchema = translator.getMetaInfo();
	}

	/**
	 * Return the RDF schema of the databas linked by the adapter.
	 * @throws DataBaseNotAccessibleException 
	 */
	public Model getLocalSchema()
	{
		return localSchema;
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
		try
		{
			if(query.getClass().getSimpleName().equals("SelectQuery"))
			{
				SelectQuery sq = (SelectQuery)query;
				Model schema_local = getLocalSchema();
				if(sq.getWhere().size()==0)
				{
					return translator.exec(query);
				}
				else{
					for(int i=0; i<sq.getWhere().size();i++)
					{
						Model res_d = execQueryDescribe(SelectQuery.createDescribeQuery(getPrefix(), sq.getWhere().get(i), SelectQuery.DROITE).getQuery(), schema_local);
						if(res_d!=null)
						{
							result_model.add(translator.exec(SelectQuery.createSimpleSelectQuery(getPrefix(), sq.getWhere().get(i), SelectQuery.DROITE)));
						}
						Model res_g = execQueryDescribe(SelectQuery.createDescribeQuery(getPrefix(), sq.getWhere().get(i), SelectQuery.GAUCHE).getQuery(), schema_local);
						if(res_g!=null)
						{
							result_model.add(translator.exec(SelectQuery.createSimpleSelectQuery(getPrefix(), sq.getWhere().get(i), SelectQuery.GAUCHE)));
						}
						Model res_m = execQueryDescribe(SelectQuery.createDescribeQuery(getPrefix(), sq.getWhere().get(i), SelectQuery.MILIEU).getQuery(), schema_local);
						if(res_m!=null)
						{
							result_model.add(translator.exec(SelectQuery.createSimpleSelectQuery(getPrefix(), sq.getWhere().get(i), SelectQuery.MILIEU)));
						}
					}
				}
			}
			else// insertQuery, DeleteQuery
			{	
				translator.exec(query);
			}
		}
		catch (QueryParseException e)
		{
			System.err.println(e.getMessage() + " :: prefix=" + getPrefix());
		}
		return result_model;
	}
}
