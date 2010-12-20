package Server.Adapter;

import java.util.Vector;

import Exception.DataBaseNotAccessibleException;
import Query.IQuery;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

public abstract class Adapter implements IAdapter {
	
	private Vector<String> prefix;
	
	public Adapter(Vector<String> prefix)
	{
		this.prefix = new Vector<String>();
		this.prefix.addAll(prefix);
	}
	
	public Vector<String> getPrefix()
	{
		return prefix;
	}
	
	@Override
	public abstract Model execute(IQuery query) throws DataBaseNotAccessibleException;

	@Override
	public abstract Model getLocalSchema() throws DataBaseNotAccessibleException;

}
