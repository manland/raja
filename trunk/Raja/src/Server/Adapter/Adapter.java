package Server.Adapter;

import java.util.List;

import Exception.DataBaseNotAccessibleException;
import Query.IQuery;
import com.hp.hpl.jena.query.ResultSet;

public abstract class Adapter implements IAdapter {
	
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
