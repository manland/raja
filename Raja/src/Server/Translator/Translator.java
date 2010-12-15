package Server.Translator;
import Server.DataBase.DataBase;
import Exception.DataBaseNotAccessibleException;
import Query.IQuery;
import Query.SelectQuery;


/**
 * Abstract class representing a query translator.
 */
public abstract class Translator implements ITranslator {
	protected IN3Translator selectTranslator;
  public Translator(DataBase dataBase, String n3File) {
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
  public RDF select(SelectQuery query) throws DataBaseNotAccessibleException {
	  return selectTranslator.select(query);
  }
  
  public RDF getMetaInfo() throws DataBaseNotAccessibleException {
	  return selectTranslator.getMetaInfo();
  }

}
