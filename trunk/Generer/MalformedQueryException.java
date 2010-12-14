
/**
 * Exception throwed when a bad syntax is releved in a query.
 */
class MalformedQueryException extends OurException {
  /**
   * The malformed query.
   */
  protected String query;

  /**
   * Contructor
   */
  public MalformedQueryException(String query, String reason) {
  }

}
