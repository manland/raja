package Query;

import Exception.MalformedQueryException;

/**
 * Interface of the Query hierarchy.
 */
public interface IQuery 
{
	/**
	 * Parse the query
	 * @param query is the query to parse
	 * @return 
	 */
	void parseQuery(String query) throws MalformedQueryException ;

	/**
	 * Rebuild and return query.
	 * @return the query.
	 */
	String getQuery() ;

}
