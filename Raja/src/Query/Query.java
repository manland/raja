package Query;

import java.util.ListIterator;
import java.util.Vector;

import Exception.MalformedQueryException;

/**
 * Abstract class representing a query.
 */
public abstract class Query implements IQuery {
	/**
	 * In which tables to execute query.
	 */
	protected Vector<String> from;

	/**
	 * Default Constructor
	 */
	public Query() 
	{
		from = new Vector<String>();
	}

	public Vector<String> getFrom()
	{
		return from;
	}

	/**
	 * Fill the fields with the given query.
	 * 
	 * @param query
	 *            the query to parse.
	 * @throws MalFormedException
	 *             if the query has a bad syntax.
	 */
	public void parseQuery(String query) throws MalformedQueryException 
	{
		from.clear();
		String res[] = query.split(",");
		if(res.length == 0)
		{
			throw new MalformedQueryException(query, "Aucun champ détecté!");
		}
		for(int i=0 ; i<res.length ; i++) 
		{
			// On enleve les espaces :
			res[i] = res[i].trim();
			if(containSpaces(res[i]))
			{
				throw new MalformedQueryException(query , res[i] + " : Le champ contient un espace!");
			}
			from.add(res[i]);
		}
	}

	/**
	 * Rebuild and return query.
	 */
	public String getQuery() 
	{
		String res = "";
		ListIterator<String> i = from.listIterator();
		if (i.hasNext())
		{
			res += i.next();
		}
		while (i.hasNext())
		{
			res += ", " + i.next();
		}
		return res;
	}

	protected boolean containSpaces(String arg) 
	{
		return arg.matches("(\\S+\\s+\\S+)+");
	}
}
