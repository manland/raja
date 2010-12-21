package Query;


import java.util.ListIterator;
import java.util.Vector;

import Exception.MalformedQueryException;

/**
 * Represent a INSERT query
 */
public class InsertQuery extends Query {
	/**
	 * List of values to insert.
	 */
	protected Vector<String> value;

	/**
	 * Default Constructor
	 */
	public InsertQuery() {
		value = new Vector<String>();
	}

	/**
	 * Parse the INSERT query.
	 */
	public void parseQuery(String query) throws MalformedQueryException {
		value.clear();

		if (!query.startsWith("INSERT INTO"))
			throw new MalformedQueryException(query,
					"Not begin with \"INSERT INTO\"");
		query = query.replace("INSERT INTO", "");
		if (!query.contains("VALUES("))
			throw new MalformedQueryException(query, "Not contain VALUES...");
		String res[] = query.split("VALUES\\(");
		super.parseQuery(res[0]);
		if(!res[1].contains(")"))
			throw new MalformedQueryException(query, "')' expected.");
		res[1] = res[1].replace(")", "");

		String values[] = res[1].split(",");
		for(int i=0 ; i< values.length ; i++) {
			values[i] = values[i].trim();
			if(containSpaces(values[i]))
				throw new MalformedQueryException(query, values[i] + " : Le champ contient un espace.");
			value.add(values[i]);
		}
	}

	public String getQuery() {
		String res = "INSERT INTO " + super.getQuery() + " VALUES(";
		ListIterator<String> i = value.listIterator();
		if (i.hasNext())
			res += i.next();
		while (i.hasNext())
			res += ", " + i.next();

		return res + ')';
	}

}
