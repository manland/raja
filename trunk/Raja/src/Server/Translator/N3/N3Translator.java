package Server.Translator.N3;
import java.util.Vector;

import Query.SelectQuery;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import de.fuberlin.wiwiss.d2rq.ModelD2RQ;

public abstract class N3Translator implements IN3Translator {
	
	private String n3File;
	private String getMetaInfo;
	private Model model;
	private Vector<String> prefix;
	
	public static final String NL = System.getProperty("line.separator");

	
	public N3Translator(String n3File, String getMetaInfo, Vector<String> prefix)
	{
		this.n3File = n3File;
		this.getMetaInfo = getMetaInfo;
		this.prefix = prefix;
		model = new ModelD2RQ(n3File);
		
	}
	
	public ResultSet select(SelectQuery query) 
	{
		String str = "";
		for(int i=0; i<prefix.size();i++){
			str+= prefix.get(i) + NL;
		}
		String requete = str+" "+query.getQuery();
		Query req = QueryFactory.create(requete) ;
		QueryExecution qexec = QueryExecutionFactory.create(req,model) ;

		ResultSet rs = qexec.execSelect() ;
		
		return rs;
	}

	@Override
	public ResultSet getMetaInfo()
	{
		SelectQuery query = new SelectQuery();
		query.setQuery(getMetaInfo);
		return select(query);
	}

}
