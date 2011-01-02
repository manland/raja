package Server.Adapter;
import java.util.Vector;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import Exception.DataBaseNotAccessibleException;
import Exception.MalformedQueryException;
import Query.DeleteQuery;
import Query.IQuery;
import Query.InsertQuery;
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
import de.fuberlin.wiwiss.d2rq.rdql.ExpressionTranslator.Result;

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
	public CompositeAdapter(Vector<Pair<String, String>> prefix, String owlFile) 
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
	 * @throws MalformedQueryException 
	 */
	public Model execute(IQuery query) throws DataBaseNotAccessibleException, MalformedQueryException 
	{		
		if(query.getClass().getSimpleName().equals("SelectQuery"))
		{
			return executeSelect(query);
		}
		else if(query.getClass().getSimpleName().equals("InsertQuery")){
			return executeInsert(query);
		}
		else if(query.getClass().getSimpleName().equals("DeleteQuery")){
			return executeInsert(query);
		}
		return null;
	}


	private Model executeSelect(IQuery query) throws DataBaseNotAccessibleException, MalformedQueryException{
		OntModel model_resultat = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
		model_resultat.add(model);
		for(int i=0; i<getPrefix().size(); i++)
		{
			model_resultat.setNsPrefix(getPrefix().get(i).getFirst(), getPrefix().get(i).getSecond());
		}

		SelectQuery sq = (SelectQuery)query;
		if(sq.getWhere().size()==0){
			for(int i=0; i<subAdapters.size();i++){
				model_resultat.add(subAdapters.get(i).execute(query));
			}
		}
		else{
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
	
			for(int i=0;i<sq.getWhere().size();i++){
				ResultSet rs = getEquivalentClassOfClass(model, sq.getWhere().get(i));
				if(rs != null){
					while(rs.hasNext()){
						QuerySolution qs = rs.nextSolution();
						Resource resource = qs.getResource("a");
						String nom = resource.getLocalName();
						String ns = resource.getNameSpace();
						String prefix = Pair.getFirstBySecond(getPrefix(), ns);
						SelectQuery req = new SelectQuery();
						req.parseQuery(query.getQuery().replace(sq.getWhere().get(i), prefix+":"+nom));
						model_resultat.add(execute(req));
					}
				}				
			}
			
		}
		Vector<Pair<String, String>> r = getEquivalentClass(model_resultat);
		Vector<Pair<Resource, Resource>> vec = new Vector<Pair<Resource,Resource>>();
//		while(r.hasNext()){
//			QuerySolution rb = r.nextSolution() ;
//			
//			Resource a = rb.getResource("c");
//			Resource b = rb.getResource("d");
//			if(a!=null && b!=null){
//				System.out.println("A : "+a.getLocalName());
//				System.out.println("B : "+b.getLocalName());
//				if(b.getLocalName().equals(a.getLocalName())){
//					Pair<Resource, Resource> paire = new Pair<Resource, Resource>(a, b);
//					vec.add(paire);
//				}
//			}
//		}
		
		for(int i=0;i<r.size();i++){
			System.out.println(r.get(i).getFirst()+" - "+r.get(i).getSecond());
		}
		
		individusIdentiques(model_resultat, vec);
		return model_resultat;
	}

	private Model executeInsert(IQuery query) throws DataBaseNotAccessibleException, MalformedQueryException{
		String nom_table ="";
		try{
			nom_table = ((InsertQuery)query).getFrom().get(0);
		}catch(ClassCastException e){
			nom_table = ((DeleteQuery)query).getFrom().get(0);
		}
		
		for(int j=0; j<getSubAdapters().size();j++)
		{
			Model res = subAdapters.get(j).getLocalSchema();
			String pref = "PREFIX rdf:<"+RDF.getURI()+"> \n PREFIX rdfs:<"+RDFS.getURI()+"> \n PREFIX m:<http://www.lirmm.fr/metaInfo#> \n";
			if(res!= null)
			{
				String qry = pref+"SELECT ?b WHERE {m:TABLE rdfs:subClassOf ?b}";
				ResultSet result = execQuerySelect(qry, res);

				int cpt=0;
				boolean ok = false;
				for(; result.hasNext() && !ok; ){ 
					QuerySolution qs = result.nextSolution();
					Resource rsc = qs.getResource("b");
					if(rsc.getLocalName().equals(nom_table)){
						cpt++;
						ok=true;
					}
				}
				if(cpt>0){
					subAdapters.get(j).execute(query);
				}
			}
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

	private Vector<Pair<String, String>> getEquivalentClass(OntModel m){
		Vector<Pair<String, String>> vec = new Vector<Pair<String,String>>();
		
		
		
		String str = "";
		for(int i=0; i<getPrefix().size();i++)
		{
			System.out.println("lll "+getPrefix().get(i));
			str += "PREFIX " + getPrefix().get(i).getFirst() + ":<" + getPrefix().get(i).getSecond() +">\n";
		}
		String req = str +
		"SELECT ?a ?b WHERE {?a owl:equivalentClass ?b}";

		Query q = QueryFactory.create(req) ;
		QueryExecution qexec = QueryExecutionFactory.create(q,m) ;
		ResultSet r = qexec.execSelect();
	//	ResultSetFormatter.out(r);
		while(r.hasNext()){
			QuerySolution qs = r.nextSolution();
			
			Resource eqClassA = qs.getResource("a");
			Resource eqClassB = qs.getResource("b");

			if(eqClassA!=null){
				
				System.out.println("pref : "+eqClassA.getNameSpace());
				
				String req2 = str +
				"SELECT ?c WHERE {?c rdf:type "+Pair.getFirstBySecond(getPrefix(), eqClassA.getNameSpace())+":"+eqClassA.getLocalName()+"}";
	
				Query q2 = QueryFactory.create(req2) ;
				QueryExecution qexec2 = QueryExecutionFactory.create(q2,m) ;
				ResultSet r2 = qexec2.execSelect();
				
				while(r2.hasNext()){
					QuerySolution qs2 = r2.nextSolution();
					Resource c = qs2.getResource("c");
					Pair<String, String> paire = new Pair<String, String>("", "");
					paire.setFirst(eqClassA.getNameSpace());
					paire.setSecond(c.getLocalName());
					vec.add(paire);
				}
			}
			if(eqClassB!=null){
				
				System.out.println("prefB : "+Pair.getFirstBySecond(getPrefix(), eqClassB.getNameSpace())+":"+eqClassB.getLocalName());
			
				String req2 = str +
				"SELECT ?c WHERE {?c rdf:type "+Pair.getFirstBySecond(getPrefix(), eqClassB.getNameSpace())+":"+eqClassB.getLocalName()+"}";
	
				Query q2 = QueryFactory.create(req2) ;
				QueryExecution qexec2 = QueryExecutionFactory.create(q2,m) ;
				ResultSet r2 = qexec2.execSelect();
				
				while(r2.hasNext()){
					QuerySolution qs2 = r2.nextSolution();
					Resource c = qs2.getResource("c");
					Pair<String, String> paire = new Pair<String, String>("", "");
					paire.setFirst(eqClassB.getNameSpace());
					System.out.println("cccc "+c.getLocalName());
					paire.setSecond(c.getLocalName());
					vec.add(paire);
				}
			}
		}
		return vec;
	}
	
	
	
	private ResultSet getEquivalentClassOfClass(Model m, String classe){
		String str = "";
		for(int i=0; i<getPrefix().size();i++)
		{
			str += "PREFIX " + getPrefix().get(i).getFirst() + ":<" + getPrefix().get(i).getSecond() +">\n";
		}
		String req = str +
		"SELECT ?a WHERE {?a owl:equivalentClass "+classe+"}";
		ResultSet r = null;
		try
		{
			Query q = QueryFactory.create(req) ;
			QueryExecution qexec = QueryExecutionFactory.create(q,m) ;
			r = qexec.execSelect() ;
		}catch(QueryParseException e){
			System.err.println(e.getMessage());
		}
		return r;
	}

	private void individusIdentiques (OntModel model, Vector<Pair<Resource, Resource>> vec)
	{
		for(int i=0;i<vec.size(); i++){
			Resource r1 = vec.get(i).getFirst();
			Resource r2 = vec.get(i).getSecond();

			String oc1_ns = r1.getNameSpace();
			String oc2_ns = r2.getNameSpace(); 
			
			
			System.out.println("r1 "+r1.getLocalName());
			System.out.println("r2 "+r2.getLocalName());
			
			if (r1.getNameSpace().equals(oc1_ns))
			{ 
				OntClass oc2 = model.getOntClass(oc2_ns+r2.getLocalName());
				System.out.println("océ NAME : "+oc2_ns+r2.getLocalName());
				Individual is1 = oc2.createIndividual(oc1_ns + r1.getLocalName());
				System.out.println("ind1 "+is1.getURI()+" R1 "+r2.getURI());
				is1.setSameAs(r2);
			}	
			else
			{ 
				System.out.println("ELSE");
				OntClass oc1 = model.getOntClass(oc1_ns+r1.getLocalName());
				System.out.println("oc1 NAME : "+oc1_ns+r1.getLocalName());

				Individual is1 = oc1.createIndividual(oc2_ns + r2.getLocalName());
				System.out.println("ind2 "+is1.getLocalName()+" R1 "+r2.getURI());
				is1.setSameAs(r1);
			}	
		}


	}
}
