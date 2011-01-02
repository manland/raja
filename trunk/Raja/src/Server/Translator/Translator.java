package Server.Translator;
import java.util.HashMap;
import java.util.Vector;

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

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;


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
		this.database = dataBase;
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
	public Model select(SelectQuery query)
	{
		return selectTranslator.select(query);
	}

	public Model getMetaInfo() throws DataBaseNotAccessibleException 
	{
		Model m = ModelFactory.createDefaultModel();
		String ns = "http://www.lirmm.fr/metaInfo#";
		m.setNsPrefix("metaInfos", ns);
		m.add(createModelFromDatabase());
		Model temp = selectTranslator.getMetaInfo();
		m.setNsPrefixes(temp.getNsPrefixMap());
		m.add(temp);
		return m;
	}

	private Model createModelFromDatabase() throws DataBaseNotAccessibleException
	{
		HashMap<String, Vector<String>> res = this.getMetaInfoFromDataBase();
		OntModel m = ModelFactory.createOntologyModel();
		
		String ns = "http://www.lirmm.fr/metaInfo#";
		m.setNsPrefix("metaInfos", ns);
		
		OntClass maSuperClasse = m.createClass(ns+"TABLE");
		
		for (String cle : res.keySet()) 
		{
			OntClass c = m.createClass(ns+cle);
			c.setSubClass(maSuperClasse);
			for(int i=0; i<res.get(cle).size();i++)
			{
				OntClass c2 = m.createClass(ns+res.get(cle).get(i));
				OntProperty prop = m.createOntProperty(ns+"COLONNE_"+c.getLocalName()+"_"+c2.getLocalName());
				prop.setRange(c2);
				prop.setDomain(c);
			}
		}
		return m;
	}
}
