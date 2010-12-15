package Query;
import java.util.Vector;

import Exception.MalformedQueryException;


/**
 * Represent a query contening WHERE
 */
abstract class ConditionalQuery extends Query {
  /**
   * Conditions on the fields
   */
  protected Vector<String> where;

  /**
   * Parse the WHERE fields
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
