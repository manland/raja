package Server.Adapter;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;

import Exception.DataBaseNotAccessibleException;
import Query.IQuery;
import com.hp.hpl.jena.query.ResultSet;

/**
 * Adaptor of several Adaptor. Needed to merge RDF informations from under Adaptor.
 */
public class CompositeAdapter extends Adapter 
{
	String owlFile;
	
	public CompositeAdapter(String owlFile) 
	{
		this.owlFile = owlFile;
		subAdapters = new Vector<IAdapter>();
		
	}

	/**
	 * Composite pattern.
	 * These are the sub-adapters managed by this adapter.
	 */
	protected Vector<IAdapter> subAdapters;

	/**
	 * Return true if query match to database
	 */
	public boolean isQueryMatching(IQuery query) 
	{
		return true;
	}

	public Vector<IAdapter> getSubAdapters() {
		return subAdapters;
	}

	public void setSubAdapters(Vector<IAdapter> subAdapters) {
		this.subAdapters = subAdapters;
	}

	/**
	 * Give the query to all sub adaptor which execute it and check if no errors occurs.
	 */
	public ResultSet execute(IQuery query) throws DataBaseNotAccessibleException 
	{
		return null;

	}

	/**
	 * Return the global RDF schema of sub adapters.
	 */
	public ResultSet getLocalSchema() 
	{
		return null;
	}
	
}
