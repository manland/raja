package Query;

import java.util.Vector;
import java.util.regex.Pattern;

import Exception.MalformedQueryException;

public class SelectQuery2 extends SelectQuery{
	
	public void parseQuery(String query) throws MalformedQueryException 
	{
		where.removeAllElements();
		selection.removeAllElements();
		triplets.removeAllElements();
		String req = "^SELECT2 ([\\?[\\w]+ ]+) WHERE ((\\{+((\\?[\\w]+ ?|[\\w]+:[\\w]+ ?){3} ?\\.? ?)+ ?\\}+( UNION )?)+)";

		Pattern pattern = Pattern.compile(req);
		java.util.regex.Matcher m = pattern.matcher(query);

		if (query.matches(req))
		{
			while(m.find())
			{
				String req_where = m.group(2);
				if(req_where.matches("\\{+((\\?[\\w]+ ?){3} ?\\}+)"))
				{
					where.removeAllElements();
				}
				else
				{
					// on rempli le vecteur de triplet en splitant par point :
					String new_re = req_where.replaceAll("\\{|\\}|UNION|","");
					
					String [] tab_trip = new_re.split(" \\. ");
					for(int i=0;i<tab_trip.length; i++){
						triplets.add(tab_trip[i]);
					}
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
			throw new MalformedQueryException(query, "Requete mal formée requete dans Select2");
		}
	}

}
