package Query;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import Exception.MalformedQueryException;

/**
 * This is a SELECT query.
 */
public class SelectQuery extends Query 
{

  private String query; 
  private Vector<String> selection = new Vector<String>();
  private Vector<String> where = new Vector<String>();
  
  public static final String DROITE = "droite";
  public static final String GAUCHE = "gauche";
  public static final String MILIEU = "milieu";
   

	/**
	 * Parse the SELECT given query.
	 */
	public void parseQuery(String query) throws MalformedQueryException 
	{
		where.removeAllElements();
		selection.removeAllElements();
		String req = "^SELECT ([\\?[\\w]+ ]+) WHERE ((\\{+((\\?[\\w]+ ?|[\\w]+:[\\w]+ ?){3} ?\\.? ?)+ ?\\}+( UNION )?)+)";

		Pattern pattern = Pattern.compile(req);
		Matcher m = pattern.matcher(query);

		if (query.matches(req))
		{
			while(m.find())
			{

				String req_where = m.group(2);
				// on remplace les accolades par des espaces : 
				String new_req = req_where.replaceAll("\\{|\\}|UNION|\\.", " ");
				String req_where_final = new_req.replaceAll(" +"," ");
				String [] tab_where = req_where_final.split(" ");
				for(int i=0; i<tab_where.length;i++)
				{
					if(tab_where[i].matches("[\\w]+:[\\w]+"))
					{
						where.add(tab_where[i]);
					}
				}
				
				// on remplit le select :
				String req_select = m.group(1);
				String [] tab_select = req_select.split(" ");
				for(int i=0; i<tab_select.length;i++)
				{
					selection.add(tab_select[i]);
				}
			}
		}
		else
		{
			System.out.println("req : "+req);
			System.out.println("query : "+query);
			throw new MalformedQueryException(query, "Requete mal formée requete : ");
		}
	}

	/**
	 * Rebuild and return query.
	 */
	public String getQuery() 
	{
		return query;
	}

	public void setQuery(String query)
	{
		this.query = query;
	}

	public Vector<String> getSelection()
	{
		return selection;
	}

	public Vector<String> getWhere()
	{
		return where;
	}

	public static SelectQuery createDescribeQuery(Vector<String> prefix, String elem, String position){
		SelectQuery query = new SelectQuery();
		String res = "";

		String str_prefix = "";
		for(int i=0; i<prefix.size();i++){					
			str_prefix+=prefix.get(i)+"\n";
		}

		res += str_prefix+" DESCRIBE ?a ?b WHERE ";
		if(position == DROITE){
			res += "{?a ?b "+elem+"}";
		}
		else if(position == MILIEU){
			res += "{?a "+elem+" ?b}";
		}
		else if(position == GAUCHE){
			res += "{"+elem+" ?a ?b}";
		}

		System.out.println("res : "+res);
		query.setQuery(res);
		return query;
	}

	public static SelectQuery selectQueryToDescribeQuery(Vector<String> prefix, SelectQuery query){

		SelectQuery sq = new SelectQuery();
		String res = "";

		String str_prefix = "";
		for(int i=0; i<prefix.size();i++){					
			str_prefix+=prefix.get(i)+"\n";
		}

		String fin = query.getQuery().substring(6);

		String str = str_prefix + "DESCRIBE "+fin;

		System.out.println("res : "+str);
		sq.setQuery(str);
		return sq;
	}

	public static String getQueryWithPrefix(Vector<String> prefix, SelectQuery q){
		String str_prefix = "";
		for(int i=0; i<prefix.size();i++){					
			str_prefix+=prefix.get(i)+"\n";
		}
		System.out.println("requte : "+str_prefix+q.getQuery());
		return str_prefix+q.getQuery();
	}
}
