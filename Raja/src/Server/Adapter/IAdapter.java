package Server.Adapter;
import java.util.List;
import java.util.Vector;

import Exception.DataBaseNotAccessibleException;
import Exception.MalformedQueryException;
import Query.IQuery;
import Query.Pair;
import Query.SelectQuery;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * Interface of the database adaptors.
 */
public interface IAdapter {
	/**
	 * Distributes the query to correspondant databases and execute it.
	 * @throws MalformedQueryException 
	 */
	Model execute(IQuery query) throws DataBaseNotAccessibleException, MalformedQueryException ;

	/**
	 * Return the local RDF schema of the adaptater.
	 */
	Model getLocalSchema()  throws DataBaseNotAccessibleException ;

	Vector<Pair<String,String>> getPrefix();
	
}
