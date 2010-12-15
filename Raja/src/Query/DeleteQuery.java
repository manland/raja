package Query;
import Exception.MalformedQueryException;


/**
 * Represent a DELETE query
 */
public class DeleteQuery extends ConditionalQuery {
  /**
   * Parse a DELETE Query
 * @return 
   */
  public  void parseQuery(String query) throws MalformedQueryException {
	  throw new MalformedQueryException("", "");
  }

  /**
   * Rebuild and return query.
   */
  public String getQuery() {
	return "";
  }

}
