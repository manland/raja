package Server.Adapter;

import java.util.Vector;

import Exception.DataBaseNotAccessibleException;
import Exception.MalformedQueryException;
import Query.DeleteQuery;
import Query.IQuery;
import Query.InsertQuery;
import Query.Pair;
import Query.SelectQuery;
import Query.SelectQuery2;
import Server.IVisiteur;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import de.fuberlin.wiwiss.d2rq.ModelD2RQ;

/**
 * Adaptor of several Adaptor. Needed to merge RDF informations from under Adaptor.
 */
public class CompositeAdapter extends Adapter 
{
	private OntModel model;

	/**
	 * Composite pattern.
	 * These are the sub-adapters managed by this adapter.
	 */
	protected Vector<IAdapter> subAdapters;	
	private String owlfile;

	public static final String NL = System.getProperty("line.separator");
	public CompositeAdapter(Vector<Pair<String, String>> prefix, String owlFile) 
	{
		super(prefix);
		subAdapters = new Vector<IAdapter>();
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
		this.owlfile = owlFile;
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
		fireGoIn();
		if(query.getClass().getSimpleName().equals("SelectQuery"))
		{
			fireGoOut();
			return executeSelect(query);
		}
		else if(query.getClass().getSimpleName().equals("SelectQuery2"))
		{
			fireGoOut();
			return execSelectQuery2(query);
		}
		else if(query.getClass().getSimpleName().equals("InsertQuery"))
		{
			fireGoOut();
			return executeInsertAndUpdate(query);
		}
		else if(query.getClass().getSimpleName().equals("DeleteQuery"))
		{
			fireGoOut();
			return executeInsertAndUpdate(query);
		}
		return null;
	}

	private Model executeSelect(IQuery query) throws DataBaseNotAccessibleException, MalformedQueryException
	{
		OntModel model_resultat = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
		model_resultat.add(model);
		for(int i=0; i<getPrefix().size(); i++)
		{
			model_resultat.setNsPrefix(getPrefix().get(i).getFirst(), getPrefix().get(i).getSecond());
		}

		SelectQuery sq = (SelectQuery)query;
		if(sq.getWhere().size()==0){
			for(int i=0; i<subAdapters.size();i++)
			{
				model_resultat.add(subAdapters.get(i).execute(query));
			}
		}
		else
		{
			for(int i=0; i<sq.getWhere().size();i++)
			{
				
				for(int j=0; j<getSubAdapters().size();j++)
				{
//					//Model resProp = subAdapters.get(j).isProperty(sq.getWhere().get(i));
//					System.out.println("--------DEBUT-------------");
//					System.out.println("COMPOSITE "+owlfile);
//					String prop = sq.getWhere().get(i);
//					String []tab = prop.split(":");
//					String pr = tab[0];
//					String p = tab[1];
//					String pref = Pair.getSecondByFirst(getPrefix(), pr);
//					
//					Pair<Model,String> resProp = subAdapters.get(j).isProperty(prop);
//					if(resProp != null){
//						System.out.println("COMPOSITE VRAI "+owlfile);
//						model_resultat.add(resProp.getFirst());
//						System.out.println("pppp "+resProp.getSecond());
//						System.out.println("aaaaa "+getLocalSchema().getProperty(pref+p).getLocalName());
//						OntProperty ontP = (OntProperty)model_resultat.getProperty(resProp.getSecond());
//						Property pp = getLocalSchema().getProperty(pref+p);
//						ontP.addSuperProperty(pp);
//					}
//					else
//					{
//						System.out.println("COMPOSITE FAUX "+owlfile);
//					}
//					System.out.println("---------FIN------------");
					Model res = subAdapters.get(j).execute(query);
					if(res!= null)
					{
						model_resultat.add(res);
					}
				}
				ResultSet rs = getEquivalentClassOfClass(model, sq.getWhere().get(i));
				if(rs != null)
				{
					while(rs.hasNext())
					{
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
		Vector<Pair<String, Resource>> r = getEquivalentClass(model_resultat);
		Vector<Pair<OntClass, OntClass>> vect_class = getOntClassIdentique(model,r);
		individusIdentiques(model_resultat, vect_class);
		return model_resultat;
	}

	private Model executeInsertAndUpdate(IQuery query) throws DataBaseNotAccessibleException, MalformedQueryException
	{
		String nom_table ="";
		try
		{
			nom_table = ((InsertQuery)query).getFrom().get(0);
		}
		catch(ClassCastException e)
		{
			nom_table = ((DeleteQuery)query).getFrom().get(0);
		}
		
		String baseName = "";
		String [] temp = nom_table.split("\\.");
		if(temp.length > 1)//horizontal mode
		{
			baseName = temp[0];
			nom_table = temp[1];
		}

		for(int j=0; j<getSubAdapters().size();j++)
		{
			Model res = subAdapters.get(j).getLocalSchema();
			String pref = "PREFIX rdf:<"+RDF.getURI()+"> \n PREFIX rdfs:<"+RDFS.getURI()+"> \n PREFIX m:<http://www.lirmm.fr/metaInfo#> \n";
			if(res!= null)
			{
				if(!baseName.isEmpty())//horizontal mode
				{
					String qry = pref+"SELECT ?d ?b WHERE {?d rdfs:subClassOf m:DATABASE . ?b rdfs:subClassOf m:TABLE}";
					ResultSet result = execQuerySelect(qry, res);
					boolean ok = false;
					for(; result.hasNext() && !ok; )
					{ 
						QuerySolution qs = result.nextSolution();
						Resource db = qs.getResource("d");
						Resource t = qs.getResource("b");
						if(db.getLocalName().equals(baseName) && t.getLocalName().equals(nom_table))
						{
							ok=true;
							return subAdapters.get(j).execute(query);
						}
					}
				}
				else//vertical mode
				{
					String qry2 = pref+"SELECT ?b WHERE {?b rdfs:subClassOf m:TABLE}";
					ResultSet result2 = execQuerySelect(qry2, res);
					boolean ok2 = false;
					for(; result2.hasNext() && !ok2; )
					{ 
						QuerySolution qs = result2.nextSolution();
						Resource rsc = qs.getResource("b");
						if(rsc.getLocalName().equals(nom_table))
						{
							ok2=true;
							return subAdapters.get(j).execute(query);
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Return the global RDF schema of sub adapters.
	 * @throws DataBaseNotAccessibleException 
	 */
	public Model getLocalSchema()
	{
		Model res = model;
		for(int i=0;i<subAdapters.size();i++)
		{
			res.add(subAdapters.get(i).getLocalSchema());
		}
		return res;
	}

	private Vector<Pair<String, Resource>> getEquivalentClass(OntModel m)
	{
		Vector<Pair<String, Resource>> vec = new Vector<Pair<String,Resource>>();

		String str = "";
		for(int i=0; i<getPrefix().size();i++)
		{
			str += "PREFIX " + getPrefix().get(i).getFirst() + ":<" + getPrefix().get(i).getSecond() +">\n";
		}
		String req = str +
		"SELECT ?a ?b WHERE {?a owl:equivalentClass ?b}";

		Query q = QueryFactory.create(req) ;
		QueryExecution qexec = QueryExecutionFactory.create(q,m) ;
		ResultSet r = qexec.execSelect();
		while(r.hasNext())
		{
			QuerySolution qs = r.nextSolution();

			Resource eqClassA = qs.getResource("a");
			Resource eqClassB = qs.getResource("b");

			if(eqClassA!=null)
			{
				String req2 = str +
				"SELECT ?c WHERE {?c rdf:type "+Pair.getFirstBySecond(getPrefix(), eqClassA.getNameSpace())+":"+eqClassA.getLocalName()+"}";

				Query q2 = QueryFactory.create(req2) ;
				QueryExecution qexec2 = QueryExecutionFactory.create(q2,m) ;
				ResultSet r2 = qexec2.execSelect();

				while(r2.hasNext())
				{
					QuerySolution qs2 = r2.nextSolution();
					Resource c = qs2.getResource("c");
					Pair<String, Resource> paire = new Pair<String, Resource>(eqClassA.getURI(), c);
					vec.add(paire);
				}
			}
			if(eqClassB!=null)
			{
				String req2 = str +
				"SELECT ?c WHERE {?c rdf:type "+Pair.getFirstBySecond(getPrefix(), eqClassB.getNameSpace())+":"+eqClassB.getLocalName()+"}";

				Query q2 = QueryFactory.create(req2) ;
				QueryExecution qexec2 = QueryExecutionFactory.create(q2,m) ;
				ResultSet r2 = qexec2.execSelect();

				while(r2.hasNext())
				{
					QuerySolution qs2 = r2.nextSolution();
					Resource c = qs2.getResource("c");
					Pair<String, Resource> paire = new Pair<String, Resource>(eqClassB.getURI(),c);
					vec.add(paire);
				}
			}
		}
		return vec;
	}

	private ResultSet getEquivalentClassOfClass(Model m, String classe)
	{
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
		}
		catch(QueryParseException e)
		{
			//System.err.println(e.getMessage());
		}
		return r;
	}


	private Vector<Pair<OntClass, OntClass>> getOntClassIdentique(OntModel modele,Vector<Pair<String, Resource>> vec)
	{
		Vector<Pair<OntClass, OntClass>> res = new Vector<Pair<OntClass,OntClass>>();
		for(int i=0;i<vec.size();i++)
		{
			Resource r1 = vec.get(i).getSecond();
			for(int j=0; j<vec.size();j++)
			{
				Resource r2 = vec.get(j).getSecond();
				if(r1.getLocalName().equals(r2.getLocalName()))
				{
					if(!vec.get(i).getFirst().equals(vec.get(j).getFirst()))
					{
						OntClass c1 = modele.getOntClass(vec.get(i).getFirst());
						OntClass c2 = modele.getOntClass(vec.get(j).getFirst());
						Pair<OntClass, OntClass> paire = new Pair<OntClass,OntClass>(c1, c2);
						res.add(paire);
					}
				}
			}
		}
		return res;
	}

	private void individusIdentiques (OntModel model, Vector<Pair<OntClass, OntClass>> vec)
	{
		for(int i=0;i<vec.size();i++)
		{
			OntClass oc1 = model.getOntClass(vec.get(i).getFirst().getURI());
			OntClass oc2 = model.getOntClass(vec.get(i).getSecond().getURI());

			String oc1_ns = oc1.getNameSpace();
			String oc2_ns = oc2.getNameSpace(); 

			ResIterator res_i = model.listSubjectsWithProperty( RDF.type, oc1 );
			while (res_i.hasNext())
			{ 
				Resource i1 = res_i.nextResource();

				if (i1.getNameSpace().equals(oc1_ns))
				{ 
					Individual is1 = oc2.createIndividual(oc2_ns + i1.getLocalName());
					is1.setSameAs(i1);
				}	
				else
				{ 
					Individual is1 = oc1.createIndividual(oc1_ns + i1.getLocalName());
					is1.setSameAs(i1);
				}	
			}
		}
	}
	
	public Pair<Model,String> isProperty(String prop) throws MalformedQueryException, DataBaseNotAccessibleException{
		for(int i=0; i<subAdapters.size();i++)
		{
			Pair<Model,String> m = subAdapters.get(i).isProperty(prop);
			if(m!=null){
				return m;
			}
		}
		return null;
	}

	@Override
	public void acceptVisitor(IVisiteur v) {
		v.visitBeforeCompositeAdapter(this);
		for(int i=0; i<subAdapters.size(); i++){
			subAdapters.get(i).acceptVisitor(v);
		}
		v.visitAfterCompositeAdapter(this);
	}

	@Override
	public String getFile() {
		return owlfile;
	}
	
	public Model execSelectQuery2(IQuery query) throws DataBaseNotAccessibleException, MalformedQueryException
	{
		OntModel model_resultat = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
		model_resultat.add(model);
		for(int i=0; i<getPrefix().size(); i++)
		{
			model_resultat.setNsPrefix(getPrefix().get(i).getFirst(), getPrefix().get(i).getSecond());
		}

		SelectQuery2 sq = (SelectQuery2)query;
		if(sq.getWhere().size()==0){
			for(int i=0; i<subAdapters.size();i++)
			{
				model_resultat.add(subAdapters.get(i).execute(query));
			}
		}
		else
		{
			for(int i=0; i<sq.getWhere().size();i++)
			{				
				for(int j=0; j<getSubAdapters().size();j++)
				{
//					//Model resProp = subAdapters.get(j).isProperty(sq.getWhere().get(i));
//					System.out.println("--------DEBUT-------------");
//					System.out.println("COMPOSITE "+owlfile);
//					String prop = sq.getWhere().get(i);
//					String []tab = prop.split(":");
//					String pr = tab[0];
//					String p = tab[1];
//					String pref = Pair.getSecondByFirst(getPrefix(), pr);
//					
//					Pair<Model,String> resProp = subAdapters.get(j).isProperty(prop);
//					if(resProp != null){
//						System.out.println("COMPOSITE VRAI "+owlfile);
//						model_resultat.add(resProp.getFirst());
//						System.out.println("pppp "+resProp.getSecond());
//						System.out.println("aaaaa "+getLocalSchema().getProperty(pref+p).getLocalName());
//						OntProperty ontP = (OntProperty)model_resultat.getProperty(resProp.getSecond());
//						Property pp = getLocalSchema().getProperty(pref+p);
//						ontP.addSuperProperty(pp);
//					}
//					else
//					{
//						System.out.println("COMPOSITE FAUX "+owlfile);
//					}
//					System.out.println("---------FIN------------");
					Model res = subAdapters.get(j).execute(query);
					if(res!= null)
					{
						model_resultat.add(res);
					}
				}
				ResultSet rs = getEquivalentClassOfClass(model, sq.getWhere().get(i));
				if(rs != null)
				{
					while(rs.hasNext())
					{
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

		individusIdentiques(model_resultat, getOntClassIdentique(model, getEquivalentClass(model_resultat)));
		

		String selec = "";
		for(int j=0; j<sq.getSelection().size();j++){
			selec+=sq.getSelection().get(j)+" ";
		}
		
		Vector<Model> models = new Vector<Model>();
		for(int i=0;i<sq.getTriplets().size();i++){
			String str_prefix = "";
			for(int j=0; j<getPrefix().size(); j++)
			{
				str_prefix+="PREFIX " + getPrefix().get(j).getFirst() + ":<" + getPrefix().get(j).getSecond() +">\n";
			}
			String q = str_prefix+"DESCRIBE "+selec+" WHERE {"+sq.getTriplets().get(i)+"}";
			Model m = null;
			Query qu = null;
			QueryExecution qexec = null;
			try
			{
				qu = QueryFactory.create(q) ;
				qexec = QueryExecutionFactory.create(qu, model_resultat) ;
				m = qexec.execDescribe();
				models.add(m);
			}
			catch (QueryParseException e)
			{
//				System.err.println(e.getMessage());
			}
		}
		
		Model mres = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
		for(int i=0; i<getPrefix().size(); i++)
		{
			mres.setNsPrefix(getPrefix().get(i).getFirst(), getPrefix().get(i).getSecond());
		}
		if(models.size()>1){
			for(int i=0; i<models.size()-1; i++){
				for(int j=i+1; j<models.size();j++){
					mres.add(models.get(i).intersection(models.get(j)));
				}
			}
		}
		return mres;
	}
	
}


