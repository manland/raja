package Server.Translator.N3;
import Query.SelectQuery;
import com.hp.hpl.jena.query.ResultSet;

public abstract class N3Translator implements IN3Translator {
	
	public N3Translator(String n3File)
	{
		
	}
	
	public ResultSet select(SelectQuery query) 
	{
		String requete = query.getQuery();
		return null;
		
	}

	@Override
	public abstract ResultSet getMetaInfo();

}
