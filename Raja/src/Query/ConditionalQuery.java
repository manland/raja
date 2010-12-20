package Query;
import java.util.Vector;

import Exception.MalformedQueryException;


/**
 * Represent a query contening WHERE
 */
abstract class ConditionalQuery extends Query 
{
	/**
	 * Conditions on the fields
	 */
	protected Vector<Pair<String, String>> where;
	protected Vector<String> connecteur;

	/**
	 * Parse the WHERE fields
	 * @return 
	 */
	public  void parseQuery(String query) throws MalformedQueryException 
	{
		throw new MalformedQueryException("", "");
	}

	/**
	 * Rebuild and return query.
	 */
	public String getQuery() 
	{
		return "";
	}

	public Vector<Pair<String,String>> getWhere() {
		return where;
	}
	
	public Vector<String> getConnecteur() {
		return connecteur;
	}
}
