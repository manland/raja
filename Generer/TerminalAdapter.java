
/**
 * Adapter directly linked to a database.
 */
class TerminalAdapter extends Adapter {
  /**
   * Translator used to convert queries to specified database.
   */
  protected ITranslator translator;

  /**
   * Return the RDF schema of the databas linked by the adapter.
   */
  public RDF getLocalSchema() {
  }

  /**
   * Return true if query match to database
   */
  public boolean isQueryMatching(IQuery query) {
  }

  /**
   * Execute the query. 
   */
  public RDF execute(IQuery query) throws DataBaseNotAccessibleException {
  }

}
