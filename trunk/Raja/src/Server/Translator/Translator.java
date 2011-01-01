package Server.Translator;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import java.util.Map.Entry;

import Server.DataBase.DataBase;
import Server.Translator.N3.IN3Translator;
import Server.Translator.N3.N3Translator;
import Exception.DataBaseNotAccessibleException;
import Query.DeleteQuery;
import Query.IQuery;
import Query.InsertQuery;
import Query.Pair;
import Query.SelectQuery;
import Query.UpdateQuery;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;


/**
 * Abstract class representing a query translator.
 */
public abstract class Translator implements ITranslator 
{
	protected IN3Translator selectTranslator;
	protected DataBase database;
	
	public Translator(DataBase dataBase, String n3File, String getMetaInfo, Vector<Pair<String, String>> prefix) 
	{
		selectTranslator = new N3Translator(n3File, getMetaInfo, prefix);
	}

	/**
	 * Execute the given query. If rs = null statement isn't execute.
	 */
	public Model exec(IQuery query) throws DataBaseNotAccessibleException 
	{
		Model res = null;
		if(query.getClass().getSimpleName().equals("SelectQuery"))
		{
			res = select((SelectQuery)query);
			
		}
		else if(query.getClass().getSimpleName().equals("InsertQuery"))
		{
			if(insert((InsertQuery)query))
			{
				res = ModelFactory.createDefaultModel();
			}
		}
		else if(query.getClass().getSimpleName().equals("UpdateQuery"))
		{
			if(update((UpdateQuery)query))
			{
				res = ModelFactory.createDefaultModel();
			}
		}
		else if(query.getClass().getSimpleName().equals("DeleteQuery"))
		{
			if(delete((DeleteQuery)query))
			{
				res = ModelFactory.createDefaultModel();
			}
		}
		return res;
	}

	/**
	 * Execute a select query.
	 */
	public Model select(SelectQuery query) throws DataBaseNotAccessibleException 
	{
		return selectTranslator.select(query);
	}

	public Model getMetaInfo() throws DataBaseNotAccessibleException 
	{
		Model m = ModelFactory.createDefaultModel();
		try {
			String ns = "http://www.lirmm.fr/metaInfo#";
			m.setNsPrefix("metaInfos", ns);
			m.add(createModelFromDatabase());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Model temp = selectTranslator.getMetaInfo();
		m.setNsPrefixes(temp.getNsPrefixMap());
		m.add(temp);
		return m;
	}

	//fair fct de transfo de string en model
	private Model createModelFromDatabase() throws SQLException{
		HashMap<String, Vector<String>> res = this.getMetaInfoFromDataBase();
		OntModel m = ModelFactory.createOntologyModel();
		
		String ns = "http://www.lirmm.fr/metaInfo#";
		m.setNsPrefix("metaInfos", ns);
		
		OntClass maSuperClasse = m.createClass(ns+"TABLE");
		
		for (String cle : res.keySet()) {
			OntClass c = m.createClass(ns+cle);
			c.setSubClass(maSuperClasse);
			for(int i=0; i<res.get(cle).size();i++){
				OntClass c2 = m.createClass(ns+res.get(cle).get(i));
				OntProperty prop = m.createOntProperty(ns+"COLONNE_"+c.getLocalName()+"_"+c2.getLocalName());
				prop.setRange(c2);
				prop.setDomain(c);
			}
		}

//		String qry ="PREFIX rdf:<"+RDF.getURI()+"> \n PREFIX rdfs:<"+RDFS.getURI()+"> \n PREFIX m:<"+ns+"> \n"+
//	//		"SELECT ?a WHERE {?prop rdf:type rdf:Property . ?prop rdfs:range ?a}";
//		"SELECT ?b WHERE {m:TABLE rdfs:subClassOf ?b}";
//		Query q = QueryFactory.create(qry) ;
//		QueryExecution qexec = QueryExecutionFactory.create(q,m) ;
//		
//		ResultSet result = qexec.execSelect() ;
//		ResultSetFormatter.out(System.out, result, q);
		
		return m;
	}
}
