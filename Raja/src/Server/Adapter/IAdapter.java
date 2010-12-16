package Server.Adapter;
import java.util.List;

import Exception.DataBaseNotAccessibleException;
import Query.IQuery;
import com.hp.hpl.jena.query.ResultSet;

/**
 * Interface of the database adaptors.
 */
public interface IAdapter {
	/**
	 * Distributes the query to correspondant databases and execute it.
	 */
	ResultSet execute(IQuery query) throws DataBaseNotAccessibleException ;

	/**
	 * Return true if query match to database
	 */
	boolean isQueryMatching(IQuery query) ;

	/**
	 * Return the local RDF schema of the adaptater.
	 */
	ResultSet getLocalSchema() ;

}
