package Server.Translator.N3;
import Query.SelectQuery;
import com.hp.hpl.jena.query.ResultSet;

public interface IN3Translator {

	ResultSet select(SelectQuery query) ;
	ResultSet getMetaInfo() ;

}
