package Query;
import java.util.Vector;

import Exception.MalformedQueryException;


/**
 * Abstract class representing a query.
 */
abstract class Query implements IQuery {
  /**
   * In which tables to execute query.
   */
  protected Vector<String> from;
  
  private boolean isSQL;

  /**
   * Fill the fields with the given query.
   * @param query the query to parse.
   * @throws MalFormedException if the query has a bad syntax.
   */
  public abstract void parseQuery(String query) throws MalformedQueryException;

  /**
   * Rebuild and return query.
   */
  public String getQuery() {
	  return "";
  }
  
  public Vector<String> getFrom() {
	  return from;
  }

}
