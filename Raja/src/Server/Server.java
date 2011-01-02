package Server;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import Exception.DataBaseNotAccessibleException;
import Exception.MalformedQueryException;
import Query.IQuery;
import Query.Pair;
import Query.SelectQuery;
import Server.Adapter.CompositeAdapter;
import Server.Indoor.IInDoor;
import Server.Indoor.IndoorFile;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * Server class.
 * Singleton
 */
public class Server {
	/**
	 * Interface to read queries and to return result.
	 */
	protected IInDoor indoor;

	/**
	 * Path of config file
	 */
	protected String configFile;

	/**
	 * Mediator of shared database who manages the distribution of the queries.
	 */
	protected CompositeAdapter mediatorLike;

	/**
	 * Protected constructor to implement singleton parttern.
	 */
	protected Server() {}

	/**
	 * Unique instance of server.
	 */
	protected static Server instance = null;

	public static Server getInstance()
	{
		if(instance == null)
		{
			instance = new Server();
		}
		return instance;
	}

	/**
	 * Initialize the server.
	 */
	public boolean init(String fileConfig, IInDoor indoor) {
		try 
		{
			parseXML(fileConfig);
		} 
		catch (DataBaseNotAccessibleException e) 
		{
			System.err.println(e.getDataBase().getDatabaseName() + " : " + e.getMessage());
			return false;
		}
		this.indoor = indoor;
		return true;
	}

	public Model getGlobalSchema()
	{
		return mediatorLike.getLocalSchema();
	}

	/**
	 * Run the server
	 */
	public void run() 
	{
		boolean fini = false;

		while(!fini)
		{
			String line = indoor.read();
			if(!line.equals("") && !line.startsWith("#"))
			{
				try 
				{
					System.out.println("Requête : "+line);
					OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF);
					for(int i=0; i<mediatorLike.getPrefix().size();i++){
						m.setNsPrefix(mediatorLike.getPrefix().get(i).getFirst(), mediatorLike.getPrefix().get(i).getSecond());
					}
					IQuery req = Factory.makeQuery(line);
					Model res = mediatorLike.execute(req);
					if(res != null)
					{
						m.add(res);
						Query q = QueryFactory.create(SelectQuery.getQueryWithPrefix(mediatorLike.getPrefix(), (SelectQuery)req)) ;
						QueryExecution qexec = QueryExecutionFactory.create(q,m) ;
						ResultSet rs = qexec.execSelect() ;
						ResultSetFormatter.out(System.out, rs, q);
						indoor.write(rs, q);
						qexec.close();
					}
				} 
				catch (DataBaseNotAccessibleException e) 
				{
					System.err.println(e.getDataBase().getDatabaseName() + " : " + e.getMessage());
				} catch (MalformedQueryException e) 
				{
					System.err.println(e.getMessage());
				}
			}
			if(line.equals(""))
			{
				fini = true;
			}
		}
	}

	private void parseXML(String fileConfig) throws DataBaseNotAccessibleException
	{
		SAXBuilder sxb = new SAXBuilder();
		Document document = null;
		try
		{
			document = sxb.build(new File(fileConfig));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		Element racine = document.getRootElement();
		List elements = racine.getChildren("CompositeAdapter");
		if(elements.size() > 0)
		{
			Element element = (Element)elements.get(0);
			String fileOWL = element.getChild("OWL").getAttributeValue("url");
			Vector<Pair<String, String>> prefix = new Vector<Pair<String, String>>();
			List e_prefix = element.getChild("Prefix").getChildren("P");
			Iterator i_p = e_prefix.iterator();
			while(i_p.hasNext())
			{
				Element p = (Element)i_p.next();
				Pair<String, String> pref = new Pair<String, String>(p.getChild("nom").getValue(), p.getChild("uri").getValue());
				prefix.add(pref);
			}
			mediatorLike = new CompositeAdapter(prefix, fileOWL);
			mediatorLike.setSubAdapters(Factory.xmlToAdapters(element.getChildren()));
		}
	}

	/**
	 * Main fonction. Entry point of this program, if you don't remember i suggest you to find another job...
	 */
	public static void main(String[] args)
	{
		if(Server.getInstance().init("bin/config.xml", new IndoorFile("bin/tests.txt","bin/out.txt")))
		{
			Server.getInstance().run();
			//Server.getInstance().getGlobalSchema().write(System.out);
		}
	}
}
