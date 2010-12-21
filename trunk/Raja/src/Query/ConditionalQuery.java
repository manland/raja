package Query;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	
	public ConditionalQuery() {
		where = new Vector<Pair<String,String>>();
		connecteur = new Vector<String>();
	}

	/**
	 * Parse the WHERE fields
	 * @return 
	 */
	public  void parseQuery(String query) throws MalformedQueryException 
	{
		where.clear();
		connecteur.clear();
		
		String parts[] = query.split("WHERE");
		if(parts.length == 1) {
			super.parseQuery(query);
			return;
		}
		super.parseQuery(parts[0]);
		query = parts[1];
//		if(!query.startsWith("WHERE "))
//			throw new MalformedQueryException(query, "Clause WHERE expected.");
//		query = query.replaceFirst("WHERE\\s+", "");
		String vars[] = query.split(",");
		for(int i=0 ; i<vars.length ; i++) {
			vars[i] = vars[i].trim();
			Pattern pop = Pattern.compile("=|<|>|<=|>=|!=|<>|^=|!<|!>");
			Matcher m = pop.matcher(vars[i]);
			if(!m.find())
				throw new MalformedQueryException(query, "No operator found in " + vars[i]);
			String op = m.group();
			connecteur.add(op.trim());
			String wheres[] = vars[i].split(op);
			where.add(new Pair<String, String>(wheres[0].trim(), wheres[1].trim()));			
		}
	}

	/**
	 * Rebuild and return query.
	 */
	public String getQuery() 
	{
		String res = super.getQuery();
		if(where.size() >= 1)
			res += " WHERE " + where.get(0).getFirst() + connecteur.get(0) + where.get(0).getSecond();
		for(int i=1 ; i<where.size() ; i++) {
			res += ", " + where.get(i).getFirst() + connecteur.get(i) + where.get(i).getSecond();
		}
		return res;
	}

	public Vector<Pair<String,String>> getWhere() {
		return where;
	}
	
	public Vector<String> getConnecteur() {
		return connecteur;
	}
}
