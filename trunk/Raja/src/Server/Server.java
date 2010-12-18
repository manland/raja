package Server;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import Exception.DataBaseNotAccessibleException;
import Server.Adapter.CompositeAdapter;
import com.hp.hpl.jena.query.ResultSet;

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
	protected Server() {
	}

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
	 * Initialize the server.
	 */
	 public boolean init(String fileConfig, IInDoor indoor) {
		 parseXML(fileConfig);
		 this.indoor = indoor;
		 return false;
	 }

	 public ResultSet getGlobalSchema()
	 {
		 return mediatorLike.getLocalSchema();
	 }

	 /**
	  * Run the server
	  */
	 public void run() {
		 boolean fini = false;

		 while(!fini)
		 {
			 String line = indoor.read();
			 if(!line.equals(""))
			 {
				 try {
					 mediatorLike.execute(Factory.makeQuery(line));
				 } catch (DataBaseNotAccessibleException e) {
					 // TODO Auto-generated catch block
					 e.printStackTrace();
				 }
			 }
			 else
			 {
				 fini = true;
			 }
		 }
	 }

	 /**
	  * Main fonction. Entry point of this program, if you don't remember i suggest you to find another job...
	  */
	 public static void main(String[] args)
	 {
		 Server s = new Server();
		 s.init("bin/config.xml", new IndoorConsole());
	 }
}
