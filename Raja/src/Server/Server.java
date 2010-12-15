package Server;
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
	public static int main(String[] args)
	{
		return 0;
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

	/**
	 * Initialize the server.
	 */
	public boolean init(IInDoor indoor) {
		this.indoor = indoor;
		return false;
	}
	
	public ResultSet getGlobalSchema()
	{
		return mediatorLike.getLocalSchema();
	}

}
