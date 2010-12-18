package Server.Adapter;

import java.util.List;
import java.util.Vector;

import Exception.DataBaseNotAccessibleException;
import Query.IQuery;
import com.hp.hpl.jena.query.ResultSet;

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
	public abstract ResultSet execute(IQuery query) throws DataBaseNotAccessibleException;

	@Override
	public abstract ResultSet getLocalSchema() throws DataBaseNotAccessibleException;

	@Override
	public abstract boolean isQueryMatching(IQuery query) throws DataBaseNotAccessibleException;
}
