package Query;
import Exception.MalformedQueryException;


/**
 * Interface of the Query hierarchy.
 */
public interface IQuery {
  /**
   * Parse the query
 * @return 
   */
   void parseQuery(String query) throws MalformedQueryException ;

  /**
   * Rebuild and return query.
   */
  String getQuery() ;

}
