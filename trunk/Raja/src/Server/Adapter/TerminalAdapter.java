package Server.Adapter;

import java.util.Vector;

import Exception.DataBaseNotAccessibleException;
import Exception.MalformedQueryException;
import Query.IQuery;
import Query.Pair;
import Query.SelectQuery;
import Server.IVisiteur;
import Server.Translator.ITranslator;

import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

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
		if(this.translator.getIsConnect())
		{
			localSchema = translator.getMetaInfo();
		}
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
		fireGoIn();
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
					fireGoOut();
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
		fireGoOut();
		return result_model;
	}
	
	public Pair<Model,String> isProperty(String prop) throws MalformedQueryException, DataBaseNotAccessibleException
	{
		String [] tab = prop.split(":");
		String p = tab[1];
		
		String pref = "PREFIX rdf:<"+RDF.getURI()+"> \n PREFIX rdfs:<"+RDFS.getURI()+"> \n PREFIX m:<http://www.lirmm.fr/metaInfo#> \n";
		String qry = pref+"SELECT ?a ?b WHERE {?a ?b m:"+p+"}";
		ResultSet result = execQuerySelect(qry, localSchema);
		boolean fin = false;
		for(; result.hasNext() && !fin; )
		{ 
			QuerySolution qs = result.nextSolution();
			Resource rsc = qs.getResource("a");
			Resource b = qs.getResource("b");
			System.out.println("TERMINAL OK"+rsc.getURI()+"   b  "+b+"  prop "+prop);
			if(rsc.getLocalName().equals(p))
			{
				for(int i=0;i<getPrefix().size();i++){
					SelectQuery q = new SelectQuery();
					String rr = "SELECT ?a ?b WHERE {?a "+getPrefix().get(i).getFirst()+":"+p+" ?b}";
					q.parseQuery(rr);
					q.setQuery(rr);
					System.out.println("reque === "+q.getQuery());
					Model m = translator.exec(q);
					if(m!=null){
						if(!m.isEmpty()){
							return new Pair<Model, String>(m,getPrefix().get(i).getSecond()+p);
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public void acceptVisitor(IVisiteur v) {
		v.visitTerminalAdapter(this);
		translator.acceptVisitor(v);
	}

	@Override
	public String getFile() {
		return translator.getN3File();
	}
	
	public ITranslator getTranslator()
	{
		return translator;
	}
}
