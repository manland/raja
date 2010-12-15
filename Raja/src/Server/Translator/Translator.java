package Server.Translator;
import Server.DataBase.DataBase;
import Server.Translator.N3.IN3Translator;
import Exception.DataBaseNotAccessibleException;
import Query.IQuery;
import Query.SelectQuery;
import com.hp.hpl.jena.query.ResultSet;


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
	public ResultSet exec(IQuery query) throws DataBaseNotAccessibleException 
	{
		return null;
	}

	protected DataBase database;

	/**
	 * Execute a select query.
	 */
	public ResultSet select(SelectQuery query) throws DataBaseNotAccessibleException {
		return selectTranslator.select(query);
	}

	public ResultSet getMetaInfo() throws DataBaseNotAccessibleException {
		return selectTranslator.getMetaInfo();
	}

}
