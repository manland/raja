import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.vocabulary.RDF;

import de.fuberlin.wiwiss.d2rq.ModelD2RQ;


public class Recensement {

	/**
	 * @param args
	 */
	public static final String NL = System.getProperty("line.separator") ;

	public static void main( String[] args ) {

		Model d2rqModel = new ModelD2RQ("file:RecencementMapping.n3");
		String recensement ="http://www.lirmm.fr/recensement#";
		String prolog_e = "PREFIX rec: <"+recensement+">" ;
		String prolog_r = "PREFIX rdf: <"+RDF.getURI()+">" ;

		// requete tous les modes de transmission virales :
		String queryString = prolog_e + NL + prolog_r + NL +
		"SELECT ?a ?b ?c "
		+ "WHERE {?a ?b ?c}" ;

		
		Query query = QueryFactory.create(queryString) ;
		QueryExecution qexec = QueryExecutionFactory.create(query, d2rqModel) ;

		System.out.println("requete tous les modes de transmission virales ");
		ResultSet rs = qexec.execSelect() ;
		ResultSetFormatter.out(System.out, rs, query);
		
		// requete tous les modes de transmission virales :
		queryString = prolog_e + NL + prolog_r + NL +
		"SELECT ?nomMal ?pays ?zone ?date ?nb WHERE {?mal rdf:type rec:MALADIE . ?mal rec:RM_LIBELLE ?nomMal  . ?mal rec:RM_LIBELLE_PAYS ?pays . ?pays rec:RP_LIBELLE_ZO ?zone . ?mal rec:RM_ID_RECENSEMENT ?re . ?re rec:RR_ANNEE ?date . ?mal rec:RM_POPULATION ?nb}" ;

	
		Query query2 = QueryFactory.create(queryString) ;
		QueryExecution qexec2 = QueryExecutionFactory.create(query2, d2rqModel) ;

		System.out.println("requete tous les modes de transmission virales ");
		ResultSet rs2 = qexec2.execSelect() ;
		ResultSetFormatter.out(System.out, rs2, query2);

	}

}
