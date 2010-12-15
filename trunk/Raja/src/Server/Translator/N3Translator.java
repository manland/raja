package Server.Translator;
import Query.SelectQuery;


public abstract class N3Translator implements IN3Translator {
	public RDF select(SelectQuery query) {
	}

	@Override
	public abstract RDF getMetaInfo();

}
