package Query;

import Exception.MalformedQueryException;






/**
 * Represent a DELETE query
 */
public class DeleteQuery extends ConditionalQuery {

	/**
	 * Default Constructor
	 */
	public DeleteQuery() {
		//	A quoi je sers???
	}
	
	/**
	 * Parse a DELETE Query
	 * 
	 * @return
	 */
	public void parseQuery(String query) throws MalformedQueryException {
		if(!query.startsWith("DELETE FROM"))
			throw new MalformedQueryException(query, "Clause 'DELETE FROM' expected.");
		query = query.replaceFirst("DELETE\\s+FROM\\s+", "");
		super.parseQuery(query);
	}

	/**
	 * Rebuild and return query.
	 */
	public String getQuery() {
		return "DELETE FROM " + super.getQuery();
	}

}
