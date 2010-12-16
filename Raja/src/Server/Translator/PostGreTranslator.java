package Server.Translator;
import java.util.Vector;

import Query.DeleteQuery;
import Query.InsertQuery;
import Query.UpdateQuery;
import Server.DataBase.DataBase;


/**
 * This is our PostGre Tanslator.
 */
public class PostGreTranslator extends Translator {
  public PostGreTranslator(DataBase dataBase, String n3File, String getMetaInfo, Vector<String> prefix) {
		super(dataBase, n3File, getMetaInfo, prefix);
		// TODO Auto-generated constructor stub
	}

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
