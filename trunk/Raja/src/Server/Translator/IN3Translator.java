package Server.Translator;
import Query.SelectQuery;


public interface IN3Translator {
  RDF select(SelectQuery query) ;

  RDF getMetaInfo() ;

}
