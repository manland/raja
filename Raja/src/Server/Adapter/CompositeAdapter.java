package Server.Adapter;
import java.util.Vector;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import Exception.DataBaseNotAccessibleException;
import Exception.MalformedQueryException;
import Query.IQuery;
import Query.SelectQuery;

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
        FileManager.get().readModel(model, "maladiesVirus.owl");
        System.out.println("CompositeAdapter::constructor::"+owlFile);
	}
	
	

	private Model execQueryDescribe(String query) {		
		Model result_model = null;
		Query q = null;
		try{
			q = QueryFactory.create(query) ;
			QueryExecution qexec = QueryExecutionFactory.create(q,model) ;
			result_model = qexec.execDescribe() ;
		}catch (QueryParseException e){
			System.out.println(e.getMessage());
		}
		return result_model;
	}
	
	public Vector<IAdapter> getSubAdapters() {
		return subAdapters;
	}

	public void setSubAdapters(Vector<IAdapter> subAdapters) {
		this.subAdapters = subAdapters;
	}

	/**
	 * Give the query to all sub adaptor which execute it and check if no errors occurs.
	 */
	public Model execute(IQuery query) throws DataBaseNotAccessibleException 
	{
		Model model_resultat = ModelFactory.createDefaultModel();
		
		if(query.getClass().getSimpleName().equals("SelectQuery")){
			SelectQuery sq = (SelectQuery)query;
				System.out.println(sq.getWhere().size()+"");
				for(int i=0; i<sq.getWhere().size();i++){
					Model res_d = execQueryDescribe(SelectQuery.createDescribeQuery(getPrefix(), sq.getWhere().get(i), SelectQuery.DROITE).getQuery());
					if(res_d!=null){
						model_resultat.add(res_d);
					}

					Model res_g = execQueryDescribe(SelectQuery.createDescribeQuery(getPrefix(), sq.getWhere().get(i), SelectQuery.GAUCHE).getQuery());
					if(res_g!= null){
						model_resultat.add(res_g);
					}
					
					Model res_m = execQueryDescribe(SelectQuery.createDescribeQuery(getPrefix(), sq.getWhere().get(i), SelectQuery.MILIEU).getQuery());
					if(res_m != null){
						model_resultat.add(res_m);
					}
					
					for(int j=0; j<getSubAdapters().size();j++){
						 Model res = subAdapters.get(j).execute(query);
						 if(res!= null){
							 model_resultat.add(res);
						 }
					}
				}
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
		for(int i=0;i<subAdapters.size();i++){
			res.add(subAdapters.get(i).getLocalSchema());
		}
		return res;
	}
	

	
}
