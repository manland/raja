package Adapter;

import Exception.DataBaseNotAccessibleException;
import Query.IQuery;

public class Adapter implements IAdapter {

	@Override
	public RDF execute(IQuery query) throws DataBaseNotAccessibleException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RDF getLocalSchema() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isQueryMatching(IQuery query) {
		// TODO Auto-generated method stub
		return false;
	}
}
