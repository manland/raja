package Server.Translator.N3;
import Query.SelectQuery;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

public interface IN3Translator {

	Model select(SelectQuery query) ;
	Model getMetaInfo() ;

}
