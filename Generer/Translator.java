
/**
 * Abstract class representing a query translator.
 */
abstract class Translator implements ITranslator {
  public Translator(DataBase dataBase, N3File file) {
  }

  /**
   * Execute the given query.
   */
  public RDF exec(IQuery query) throws DataBaseNotAccessibleException {
  }

  protected DataBase database;

  /**
   * Execute a select query.
   */
  protected  select(SelectQuery query) throws DataBaseNotAccessibleException {
  }

}
