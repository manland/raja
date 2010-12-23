package Query;

import java.util.ListIterator;
import java.util.Vector;

import Exception.MalformedQueryException;

/**
 * Represent a UPDATE Query.
 */
public class UpdateQuery extends ConditionalQuery 
{
	/**
	 * Contains the fields modifications
	 */
	protected Vector<Pair<String, String>> set;

	/**
	 * Default Constructor
	 */
	public UpdateQuery() 
	{
		set = new Vector<Pair<String, String>>();
	}

	public Vector<Pair<String, String>> getSet()
	{
		return set;
	}

	/**
	 * Parse the UPDATE query.
	 */
	public void parseQuery(String query) throws MalformedQueryException 
	{
		if(!query.startsWith("UPDATE"))
		{
			throw new MalformedQueryException(query, "Clause 'UPDATE' expected.");
		}
		if(!query.contains("SET"))
		{
			throw new MalformedQueryException(query, "Clause 'SET' expected.");
		}
		query = query.replaceFirst("UPDATE\\s+", "");
		String parts[] = query.split("SET|WHERE");
		if(parts.length == 2) 
		{
			super.parseQuery(parts[0]);
			query = parts[1];
		}
		else 
		{
			super.parseQuery(parts[0] + " WHERE " + parts[2]);
			query = parts[1];
		}

		String vars[] = query.split(",");
		for(int i=0 ; i<vars.length ; i++) 
		{
			String params[] = vars[i].split("=");
			if(params.length != 2)
			{
				throw new MalformedQueryException(query, "'=' expected in " + vars[i]);
			}
			if(containSpaces(params[0]))
			{
				throw new MalformedQueryException(query, params[0] + " contains spaces.");
			}
			if(containSpaces(params[1]))
			{
				throw new MalformedQueryException(query, params[1] + " contains spaces.");
			}
			set.add(new Pair<String, String>(params[0].trim(), params[1].trim()));
		}
	}

	/**
	 * Rebuild and return query.
	 */
	public String getQuery() 
	{
		String res = super.getQuery();
		String parts[] = res.split("WHERE");
		res = "UPDATE " + parts[0] + " SET ";
		ListIterator<Pair<String, String>> i = set.listIterator();
		if(i.hasNext()) 
		{
			Pair<String, String>  p = i.next();
			res += p.getFirst() + "=" + p.getSecond();
		}
		while(i.hasNext()) 
		{
			Pair<String, String> p = i.next();
			res += ", " + p.getFirst() + "=" + p.getSecond();
		}
		if(parts.length == 2)
		{
			res += " WHERE " + parts[1];
		}
		return res;
	}
}
