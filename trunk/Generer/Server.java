
/**
 * Server class.
 * Singleton
 */
interface Server {
  /**
   * Protected constructor to implement singleton parttern.
   */
  protected Server() {
  }

  /**
   * Run the server
   */
  public  run() {
  }

  /**
   * Main fonction. Entry point of this program, if you don't remember i suggest you to find another job...
   */
  public static int main(string[] args)
  {
  }

  /**
   * Unique instance of server.
   */
  protected static Server instance;

  /**
   * Initialize the server.
   */
  public boolean init() {
  }

  /**
   * Interface to read queries and to return result.
   */
  protected IInDoor interface;

  /**
   * Path of config file
   */
  protected String configFile;

  /**
   * Global Schema RDF to offer a view on the databases. 
   */
  protected RDF globalSchema;

  /**
   * Mediator of shared database who manages the distribution of the queries.
   */
  protected CompositeAdapter mediatorLike;

}
