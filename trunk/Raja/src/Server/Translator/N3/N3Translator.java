package Server.Translator.N3;
import java.util.Vector;

import Query.SelectQuery;
import com.hp.hpl.jena.query.ResultSet;

public abstract class N3Translator implements IN3Translator {
	
	private String n3File;
	private String getMetaInfo;
	private Vector<String> prefix;
	
	public N3Translator(String n3File, String getMetaInfo, Vector<String> prefix)
	{
		this.n3File = n3File;
		this.getMetaInfo = getMetaInfo;
		this.prefix = prefix;
	}
	
	public ResultSet select(SelectQuery query) 
	{
		String requete = query.getQuery();
		return null;
	}

	@Override
	public ResultSet getMetaInfo()
	{
		return null;
	}

}
