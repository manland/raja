package Query;
import java.util.Vector;

import Exception.MalformedQueryException;


/**
 * Represent a UPDATE Query.
 */
public class UpdateQuery extends ConditionalQuery {
  /**
   * Contains the fields modifications
   */
  protected Vector<String> set;

  /**
   * Parse the UPDATE query.
   */
  public void parseQuery(String query) throws MalformedQueryException {
	  throw new MalformedQueryException("", "");
  }

  /**
   * Rebuild and return query.
   */
  public String getQuery() {
	  return "";
  }

}
