package Query;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import Exception.MalformedQueryException;

/**
 * This is a SELECT query.
 */
public class SelectQuery implements IQuery {

  private String query; 
  private Vector<String> selection = new Vector<String>();
  private Vector<String> where = new Vector<String>();

  
  /**
   * Parse the SELECT given query.
   */
  public void parseQuery(String query) throws MalformedQueryException {
	  where.removeAllElements();
	  selection.removeAllElements();
	  String req = "^SELECT ([\\?[a-zA-Z_]+ ]+) WHERE \\{+([[[\\??[a-zA-Z_]+ {0,1}|[a-zA-Z_]+:[a-zA-Z_]+ {0,1}]{3}] \\. ]+)\\}+";
	  Pattern pattern = Pattern.compile(req);
	  Matcher m = pattern.matcher(query);
//	      [\\{+[[\\??[a-zA-Z_]+:[a-zA-Z_]+ ]{3}\\.?]+\\}+ [UNION]? ]+
	  
//	^SELECT ([\\?[a-zA-Z_]+ ]+) WHERE \\{+([((\\??[a-zA-Z_]+ {0,1}|[a-zA-Z_]+:[a-zA-Z_]+ {0,1}){3}) \\. ])+\\}+
	  if (query.matches(req))
	  {
		  while(m.find()){
			  String req_where = m.group(2);
			  String [] tab_where = req_where.split("\\.");
			  for(int i=0; i<tab_where.length;i++){
				  String [] tab_temp = tab_where[i].split(" ");
				  for(int j=0; j<tab_temp.length;j++){
					  if(tab_temp[j].matches("[a-zA-Z_]+:[a-zA-Z_]+ {0,1}")){
						  where.add(tab_temp[j]);
					  }
				  }
			  }
			  System.out.println("taille where : "+tab_where.length);
			  String req_select = m.group(1);
			  String [] tab_select = req_select.split(" ");
			  for(int i=0; i<tab_select.length;i++){
				  selection.add(tab_select[i]);
			  }
		  }
	  }
	  else
	  {
		  throw new MalformedQueryException(query, "Requete mal formée");
	  }
  }

  /**
   * Rebuild and return query.
   */
  public String getQuery() {
	  return query;
  }

  public void setQuery(String query){
	  this.query = query;
  }
  
  public Vector<String> getSelection(){
	  return selection;
  }
  
  public Vector<String> getWhere(){
	  return where;
  }
  
}
