package Server.Adapter;

import java.util.List;
import java.util.Vector;

import Exception.DataBaseNotAccessibleException;
import Query.IQuery;
import com.hp.hpl.jena.query.ResultSet;

public abstract class Adapter implements IAdapter {
	
	private Vector<String> prefix;
	public Vector<String> getPrefix()
	{
		return prefix;
	}
	
	public Adapter()
	{
	}
	
	@Override
	public ResultSet execute(IQuery query) throws DataBaseNotAccessibleException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet getLocalSchema() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isQueryMatching(IQuery query) {
		// TODO Auto-generated method stub
		return false;
	}
}
