
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
   */
  public  parseQuery(String query) throws MalformedQueryException {
  }

  /**
   * Rebuild and return query.
   */
  public String getQuery() {
  }

}
