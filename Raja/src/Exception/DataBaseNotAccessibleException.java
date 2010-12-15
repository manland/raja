package Exception;
import Server.DataBase.DataBase;;


/**
 * Exception throwed when a database is not accessible anymore.
 */
public class DataBaseNotAccessibleException extends OurException {
  /**
   * Constructor. Bref ne nous attardons pas l� dessus je ne pense pas que �a vaille le coup...
   */
  public DataBaseNotAccessibleException(DataBase database, String reason) {
	  super(reason);
  }

  /**
   * The database which is not accessible.
   */
  protected DataBase database;

}
