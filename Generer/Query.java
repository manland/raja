
/**
 * Abstract class representing a query.
 */
interface Query {
  /**
   * In which tables to execute query.
   */
  protected Vector<String> from;

  /**
   * Fill the fields with the given query.
   * @param query the query to parse.
   * @throws MalFormedException if the query has a bad syntax.
   */
  public  parseQuery(String query) throws MalformedQueryException {
  }

  /**
   * Rebuild and return query.
   */
  public String getQuery() {
  }

  private boolean isSQL;

}
