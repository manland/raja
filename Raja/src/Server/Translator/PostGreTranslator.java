package Server.Translator;
import Query.DeleteQuery;
import Query.InsertQuery;
import Query.UpdateQuery;


/**
 * This is our PostGre Tanslator.
 */
public class PostGreTranslator extends Translator {
  /**
   * Execute a delete query.
   */
  public boolean delete(DeleteQuery query) {
	return false;
  }

  /**
   * Execute a insert query.
   */
  public boolean insert(InsertQuery query) {
	return false;
  }

  /**
   * Execute a update query.
   */
  public boolean update(UpdateQuery query) {
	return false;
  }

}
