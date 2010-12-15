package Query;
import java.util.Vector;

import Exception.MalformedQueryException;


/**
 * This is a SELECT query.
 */
public class SelectQuery extends ConditionalQuery {
  /**
   * Fields selected by the query.
   */
  protected Vector<String> selected;

  /**
   * Parse the SELECT given query.
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
