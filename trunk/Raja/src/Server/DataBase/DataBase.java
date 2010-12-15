package Server.DataBase;


/**
 * Classe packaging information on a database.
 */
public class DataBase {
  /**
   * Ip address or url of database server.
   */
  protected String address;

  /**
   * User name used to connect to database.
   */
  protected String userName;

  /**
   * Port used to connect to the database.
   */
  protected int port;

  /**
   * Type of database.
   */
  protected DataBaseType type;

  /**
   * Password used to connect to database.
   */
  protected String passWord;

  /**
   * Name of database.
   */
  protected String databaseName;

  /**
   * Parse information from the config file to fill information on the database.
   */
  public void parse(String configline) {
  }

}
