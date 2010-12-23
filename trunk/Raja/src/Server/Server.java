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
import Query.SelectQuery;
import Server.Adapter.CompositeAdapter;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.rdf.model.Model;

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
		parseXML(fileConfig);
		this.indoor = indoor;
		return false;
	}

	public Model getGlobalSchema()
	{
		Model m = null;
		try {
			m = mediatorLike.getLocalSchema();
		} catch (DataBaseNotAccessibleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m;
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
			if(!line.equals(""))
			{
				try 
				{
					Model m = mediatorLike.execute(Factory.makeQuery(line));
					SelectQuery sq = new SelectQuery();
					sq.setQuery(line);
					Query q = QueryFactory.create(SelectQuery.getQueryWithPrefix(mediatorLike.getPrefix(), sq)) ;
					QueryExecution qexec = QueryExecutionFactory.create(q,m) ;
					ResultSet rs = qexec.execSelect() ;
					ResultSetFormatter.out(System.out, rs, q);
				} 
				catch (DataBaseNotAccessibleException e) 
				{
					e.printStackTrace();
				} catch (MalformedQueryException e) 
				{
					e.printStackTrace();
				}
			}
			else
			{
				fini = true;
			}
		}
	}

	private void parseXML(String fileConfig)
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
			Vector<String> prefix = new Vector<String>();
			List e_prefix = element.getChild("Prefix").getChildren("P");
			Iterator i_p = e_prefix.iterator();
			while(i_p.hasNext())
			{
				Element p = (Element)i_p.next();
				prefix.add(p.getValue());
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
		Server.getInstance().init("bin/config.xml", new IndoorFile("bin/tests.txt","bin/out.txt"));
		Server.getInstance().run();
		//Server.getInstance().getGlobalSchema().write(System.out);
	}
}