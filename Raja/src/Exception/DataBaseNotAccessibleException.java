package Exception;

import Server.DataBase.DataBase;;

/**
 * Exception throwed when a database is not accessible anymore.
 */
public class DataBaseNotAccessibleException extends OurException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 990135160158791237L;
	/**
	 * The database which is not accessible.
	 */
	protected DataBase database;

	/**
	 * Constructor.
	 */
	public DataBaseNotAccessibleException(DataBase database, String reason) 
	{
		super(reason);
		this.database = database;
	}

	public DataBase getDataBase()
	{
		return database;
	}
}
