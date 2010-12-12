// constructeurs OWL
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.sparql.util.IndentedWriter;
//import com.hp.hpl.jena.util.FileManager;

// constructeurs de rdf, rdfs, owl
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.*;

import de.fuberlin.wiwiss.d2rq.ModelD2RQ;


public class ModeleOntologique {

	/**
	 * @param args
	 */
	
	public static final String NL = System.getProperty("line.separator");
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		OntModel model_onto = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);

		
		// use the file manager to read an RDF document into the model
		Model d2rqModelMaladieFacteur = new ModelD2RQ("file:tp4Mapping.n3");
		Model d2rqModelVirusTransmission = new ModelD2RQ("file:tp3Mapping.n3");

		model_onto.add(d2rqModelMaladieFacteur);
		model_onto.add(d2rqModelVirusTransmission);

		String str_virus ="http://www.lirmm.fr/virus#";
		String str_maladie="http://www.lirmm.fr/maladie#";

		model_onto.setNsPrefix("virus", str_virus);
		model_onto.setNsPrefix("maladie", str_maladie);
//		
//		// création des classes du modèle ontologique : 
		OntClass maladie = model_onto.getOntClass(str_maladie+"MALADIE");
		OntClass facteur = model_onto.getOntClass(str_maladie+"FACTEUR");
		OntClass pathogene = model_onto.createClass(str_maladie+"Pathogene");
		
		OntClass virus = model_onto.getOntClass(str_virus+"Virus");
		OntClass bacterie =  model_onto.createClass(str_virus+"Bacterie");
//		
		OntClass maladie_virale = model_onto.createClass(str_maladie+"MaladieVirale");
		OntClass maladie_bacterienne = model_onto.createClass(str_maladie+"MaladieBacterienne");
//		
//		// ajout de l'héritage entre les classes :
		maladie_bacterienne.setSuperClass(maladie);
		maladie_virale.setSuperClass(maladie);
		pathogene.setSuperClass(facteur);
		virus.setSuperClass(facteur);
		bacterie.setSuperClass(pathogene);
//		
//		// creation de la propriete APrOrigine : 
		ObjectProperty a_pr_origine = model_onto.createObjectProperty(str_maladie+"APrOrigine");
		
	    SomeValuesFromRestriction assoc1 = model_onto.createSomeValuesFromRestriction(null, a_pr_origine, virus);
	    MinCardinalityRestriction min_card = model_onto.createMinCardinalityRestriction(null, a_pr_origine, 1);
		maladie_virale.addSuperClass(assoc1);
		maladie_virale.addSuperClass(min_card);
//		
	    SomeValuesFromRestriction assoc2 = model_onto.createSomeValuesFromRestriction(null, a_pr_origine, bacterie);
	    maladie_bacterienne.addSuperClass(assoc2);
	    maladie_bacterienne.addSuperClass(min_card);
		
	    
//		System.out.println(maladie.getLocalName());
//		
//		// requete : 
//		
		String prolog_m = "PREFIX maladie: <"+str_maladie+">" ;
		String prolog_e = "PREFIX virus: <"+str_virus+">" ;
		String prolog_r = "PREFIX rdf: <"+RDF.getURI()+">" ;
		String prolog_rdfs = "PREFIX rdfs: <"+RDFS.getURI()+">" ;
		String prolog_owl = "PREFIX owl: <"+OWL.getURI()+">" ;

		// Maladie bact du systeme : 
//		String queryString2 = prolog_m + NL + prolog_r + NL + prolog_e + NL + prolog_rdfs + NL +
//		"SELECT DISTINCT ?maladie "+  
//		 "WHERE  { ?facteur maladie:TYPEFACTEUR maladie:Bacterie . " +
//		 			"?origine maladie:ESTFACTEURDE ?facteur . " +
//		 			"?origine maladie:ESTORIGINEDE ?maladie}";
		
		//tous les virus du systeme avec leur sourche virale et leur type de genome: 
		String queryString2 = prolog_m + NL + prolog_r + NL + prolog_e + NL + prolog_rdfs + NL +prolog_owl + NL +
		"SELECT ?a ?b ?c WHERE{?a ?b ?c}";
		//affiche pour tous les maladies, leur souche
	//	"SELECT ?souche ?facteur WHERE {?souche maladie:ESTORIGINEDE ?facteur}";
		//affiche pour tous les virus, leur souche
	//	"SELECT ?virus ?souche WHERE {?virus maladie:ESTFACTEURDE ?souche}";
		
		// tous les virus du systeme : 
	//	"SELECT ?virus WHERE {?virus rdf:type maladie:FACTEUR }";
		
		System.out.println("Table générale : ");
		Query query = QueryFactory.create(queryString2) ;
		QueryExecution qexecMysql = QueryExecutionFactory.create(query,model_onto) ;

		ResultSet rsMysql = qexecMysql.execSelect() ;
		System.out.println("Pour Mysql : ");
		ResultSetFormatter.out(System.out, rsMysql, query);

//		try {       
//	    	 FileOutputStream outStream = new FileOutputStream("maladies.n3");
//            // exporte le resultat dans un fichier
//            model_onto.write(outStream, "N3");
//            outStream.close();
//		  }
//	      catch (FileNotFoundException e) {System.out.println("File not found");}
//	      catch (IOException e) {System.out.println("IO problem");}
		

	}

}
