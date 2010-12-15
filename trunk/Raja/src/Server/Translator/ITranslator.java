package Server.Translator;
import Exception.DataBaseNotAccessibleException;
import Query.DeleteQuery;
import Query.IQuery;
import Query.InsertQuery;
import Query.SelectQuery;
import Query.UpdateQuery;


/**
 * Interface of the Translator hierarchy.
 */
public interface ITranslator {
  /**
   * Return the RDF shema containing meta-information on the database.
   */
  RDF getMetaInfo() ;

  /**
   * Execute a select query.
   */
  RDF select(SelectQuery query) throws DataBaseNotAccessibleException ;

  /**
   * Execute a insert query.
   */
  boolean insert(InsertQuery query) throws DataBaseNotAccessibleException ;

  /**
   * Execute a delete query.
   */
  boolean delete(DeleteQuery query) throws DataBaseNotAccessibleException ;

  /**
   * Execute a update query.
   */
  boolean update(UpdateQuery query) throws DataBaseNotAccessibleException ;

  /**
   * Execute the given query
   */
  RDF exec(IQuery query) throws DataBaseNotAccessibleException ;

}
