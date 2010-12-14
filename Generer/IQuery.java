
/**
 * Interface of the Query hierarchy.
 */
interface IQuery {
  /**
   * Parse the query
   */
   parseQuery(String query) throws MalformedQueryException ;

  /**
   * Rebuild and return query.
   */
  String getQuery() ;

}
