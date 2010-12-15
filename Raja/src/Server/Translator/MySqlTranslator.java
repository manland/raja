package Server.Translator;
import Query.DeleteQuery;
import Query.InsertQuery;
import Query.UpdateQuery;


/**
 * This is our MySql Tanslator.
 */
public class MySqlTranslator extends Translator {
  /**
   * Execute a insert query.
   */
  public boolean insert(InsertQuery query) {
	return false;
  }

  /**
   * Execute a delete query.
   */
  public boolean delete(DeleteQuery query) {
	return false;
  }

  /**
   * Execute a update query.
   */
  public boolean update(UpdateQuery query) {
	return false;
  }

}
