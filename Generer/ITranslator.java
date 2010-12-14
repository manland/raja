
/**
 * Interface of the Translator hierarchy.
 */
interface ITranslator {
  /**
   * Return the RDF shema containing meta-information on the database.
   */
  RDF getMetaInfo() ;

  /**
   * Execute a select query.
   */
  protected  select(SelectQuery query) throws DataBaseNotAccessibleException ;

  /**
   * Execute a insert query.
   */
  protected  insert(InsertQuery query) throws DataBaseNotAccessibleException ;

  /**
   * Execute a delete query.
   */
  protected  delete(DeleteQuery query) throws DataBaseNotAccessibleException ;

  /**
   * Execute a update query.
   */
  protected  update(UpdateQuery query) throws DataBaseNotAccessibleException ;

  /**
   * Execute the given query
   */
  RDF exec(IQuery query) throws DataBaseNotAccessibleException ;

  protected IN3Translator selectTranslator;

}
