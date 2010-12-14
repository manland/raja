
/**
 * Represent a UPDATE Query.
 */
class UpdateQuery extends ConditionalQuery {
  /**
   * Contains the fields modifications
   */
  protected Vector<String> set;

  /**
   * Parse the UPDATE query.
   */
  public  parseQuery(String query) throws MalformedQueryException {
  }

  /**
   * Rebuild and return query.
   */
  public String getQuery() {
  }

}
