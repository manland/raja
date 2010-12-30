package Server.Adapter;
import java.util.Vector;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import Exception.DataBaseNotAccessibleException;
import Exception.MalformedQueryException;
import Query.IQuery;
import Query.Pair;
import Query.SelectQuery;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.sparql.util.IndentedWriter;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import de.fuberlin.wiwiss.d2rq.ModelD2RQ;

/**
 * Adaptor of several Adaptor. Needed to merge RDF informations from under Adaptor.
 */
public class CompositeAdapter extends Adapter 
{
	private String owlFile;
	private Model model;

	/**
	 * Composite pattern.
	 * These are the sub-adapters managed by this adapter.
	 */
	protected Vector<IAdapter> subAdapters;	

	public static final String NL = System.getProperty("line.separator");
	public CompositeAdapter(Vector<String> prefix, String owlFile) 
	{
		super(prefix);
		this.owlFile = owlFile;
		subAdapters = new Vector<IAdapter>();
		model = ModelFactory.createDefaultModel();
		FileManager.get().readModel(model, owlFile);
	}

	

	public Vector<IAdapter> getSubAdapters() 
	{
		return subAdapters;
	}

	public void setSubAdapters(Vector<IAdapter> subAdapters) 
	{
		this.subAdapters = subAdapters;
	}

	/**
	 * Give the query to all sub adaptor which execute it and check if no errors occurs.
	 */
	public Model execute(IQuery query) throws DataBaseNotAccessibleException 
	{
		OntModel model_resultat = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
		model_resultat.setNsPrefix("maladie", "http://www.lirmm.fr/maladie#");
		model_resultat.setNsPrefix("virus", "http://www.lirmm.fr/virus#");
		
		if(query.getClass().getSimpleName().equals("SelectQuery"))
		{
			SelectQuery sq = (SelectQuery)query;
			for(int i=0; i<sq.getWhere().size();i++)
			{
				Model res_d = execQueryDescribe(SelectQuery.createDescribeQuery(getPrefix(), sq.getWhere().get(i), SelectQuery.DROITE).getQuery(), model);
				if(res_d!=null)
				{

					model_resultat.add(res_d);
				}

				Model res_g = execQueryDescribe(SelectQuery.createDescribeQuery(getPrefix(), sq.getWhere().get(i), SelectQuery.GAUCHE).getQuery(),model);
				if(res_g!= null)
				{
					model_resultat.add(res_g);
				}

				Model res_m = execQueryDescribe(SelectQuery.createDescribeQuery(getPrefix(), sq.getWhere().get(i), SelectQuery.MILIEU).getQuery(),model);
				if(res_m != null)
				{
					model_resultat.add(res_m);
				}

				for(int j=0; j<getSubAdapters().size();j++)
				{
					Model res = subAdapters.get(j).execute(query);
					if(res!= null)
					{
						model_resultat.add(res);
					}
				}
			}
			
			ResultSet r = getEquivalentClass(model_resultat);
			Vector<Pair<Resource, Resource>> vec = new Vector<Pair<Resource,Resource>>();
			while(r.hasNext()){
	            QuerySolution rb = r.nextSolution() ;
	            
	            Resource a = rb.getResource("a");
	            Resource b = rb.getResource("b");
	            Pair<Resource, Resource> paire = new Pair<Resource, Resource>(a, b);
	            vec.add(paire);	            
			}
            individusIdentiques(model_resultat, vec);

			
			return model_resultat;
		}
		return null;
	}
	
	/**
	 * Return the global RDF schema of sub adapters.
	 * @throws DataBaseNotAccessibleException 
	 */
	public Model getLocalSchema() throws DataBaseNotAccessibleException 
	{
		Model res = model;
		for(int i=0;i<subAdapters.size();i++)
		{
			res.add(subAdapters.get(i).getLocalSchema());
		}
		return res;
	}
	
	private ResultSet getEquivalentClass(OntModel m){
		String str = "";
		for(int i=0; i<getPrefix().size();i++)
		{
			str += getPrefix().get(i) +"\n";
		}
		String req = str +
			"SELECT ?a ?b WHERE {?a owl:equivalentClass ?b}";
		
		Query q = QueryFactory.create(req) ;
		QueryExecution qexec = QueryExecutionFactory.create(q,m) ;
		ResultSet r = qexec.execSelect() ;

		return r;
	}
	
	private void individusIdentiques (OntModel model, Vector<Pair<Resource, Resource>> vec)
	{
		for(int i=0;i<vec.size(); i++){
			Resource r1 = vec.get(i).getFirst();
			Resource r2 = vec.get(i).getSecond();
			
			String oc1_ns = r1.getNameSpace();
			String oc2_ns = r2.getNameSpace(); 

			if (r1.getNameSpace().equals(oc1_ns))
			{ 
				OntClass oc2 = model.getOntClass(oc2_ns+r2.getLocalName());
				Individual is1 = oc2.createIndividual(oc2_ns + r1.getLocalName());
				is1.setSameAs(r1);
			}	
			else
			{ 
				OntClass oc1 = model.getOntClass(oc1_ns+r1.getLocalName());
				Individual is1 = oc1.createIndividual(oc1_ns + r1.getLocalName());
				is1.setSameAs(r1);
			}	
		}
		
		
	}
}
