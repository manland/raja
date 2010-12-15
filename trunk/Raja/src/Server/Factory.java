package Server;

import Query.IQuery;
import Server.DataBase.DataBase;
import Server.Translator.ITranslator;

/**
 * Factory used to create instances of objects issued of complex class hierarchy.
 */
public class Factory {
	/**
	 * Create and return Query object matching with the given query.
	 */
	public static IQuery makeQuery(String query)
	{
		return null;
	}

	/**
	 * Create and return the Traductor matching with the given database.
	 */
	public static ITranslator makeTraductor(DataBase database)
	{
		return null;
	}

}

