package Server.Adapter;
import java.util.List;

import Exception.DataBaseNotAccessibleException;
import Query.IQuery;
import Query.SelectQuery;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * Interface of the database adaptors.
 */
public interface IAdapter {
	/**
	 * Distributes the query to correspondant databases and execute it.
	 */
	Model execute(IQuery query) throws DataBaseNotAccessibleException ;

	/**
	 * Return the local RDF schema of the adaptater.
	 */
	Model getLocalSchema()  throws DataBaseNotAccessibleException ;

}
