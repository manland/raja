package Server.Translator;
import Exception.DataBaseNotAccessibleException;
import Query.DeleteQuery;
import Query.IQuery;
import Query.InsertQuery;
import Query.SelectQuery;
import Query.UpdateQuery;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;


/**
 * Interface of the Translator hierarchy.
 */
public interface ITranslator {
  /**
   * Return the RDF shema containing meta-information on the database.
   */
	Model getMetaInfo() throws DataBaseNotAccessibleException ;

  /**
   * Execute a select query.
   */
	Model select(SelectQuery query) throws DataBaseNotAccessibleException ;

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
  Model exec(IQuery query) throws DataBaseNotAccessibleException ;

}
