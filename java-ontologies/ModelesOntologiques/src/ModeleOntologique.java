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
		Model d2rqModelMaladieFacteur = new ModelD2RQ("file:MaladieFacteurMapping.n3");
		
		Model d2rqModelVirusTransmission = new ModelD2RQ("file:VirusTransmissionMapping.n3");
		
		
		model_onto.add(d2rqModelMaladieFacteur);
		model_onto.add(d2rqModelVirusTransmission);
	
		String str_virus ="http://www.lirmm.fr/virus#";
		String str_maladie="http://www.lirmm.fr/maladie#";

		model_onto.setNsPrefix("virus", str_maladie);
		model_onto.setNsPrefix("maladie", str_maladie);
//		
//		// création des classes du modèle ontologique : 
		OntClass maladie = model_onto.createClass(str_maladie+"MALADIE");
		OntClass facteur = model_onto.createClass(str_maladie+"FACTEUR");
		OntClass pathogene = model_onto.createClass(str_maladie+"PATHOGENE");
		OntClass facteurViraux = model_onto.createClass(str_maladie+"FACTEURVIRAUX");
		
		OntClass virus = model_onto.createClass(str_virus+"VIRUS");
		OntClass bacterie =  model_onto.createClass(str_virus+"BACTERIE");
//		
		OntClass maladie_virale = model_onto.createClass(str_maladie+"MALADIEVIRALE");
		OntClass maladie_bacterienne = model_onto.createClass(str_maladie+"MALADIEBACTERIENNE");
//		
//		// ajout de l'héritage entre les classes :
		
		facteurViraux.addEquivalentClass(virus);
		maladie.addEquivalentClass(maladie_virale);
		
		maladie_bacterienne.setSuperClass(maladie);
		maladie_virale.setSuperClass(maladie);
		pathogene.setSuperClass(facteur);
		facteurViraux.setSuperClass(facteur);
		virus.setSuperClass(facteurViraux);
		bacterie.setSuperClass(pathogene);

		

	
//		
//		// creation de la propriete APrOrigine : 
		ObjectProperty a_pr_origine = model_onto.createObjectProperty(str_maladie+"APRORIGINE");
		
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
		
		//tous les virus du systeme avec leur sourche virale et leur type de genome: 
		String queryString2 = prolog_m + NL + prolog_r + NL +  prolog_rdfs + NL +prolog_owl + NL + prolog_e + NL+
		"SELECT ?a ?b ?c WHERE{?a ?b ?c}";
		
		System.out.println("Table générale : ");
		Query query = QueryFactory.create(queryString2) ;
		QueryExecution qexecMysql = QueryExecutionFactory.create(query,model_onto.getBaseModel()) ;

		ResultSet rsMysql = qexecMysql.execSelect() ;
		System.out.println("Pour Mysql : ");
		ResultSetFormatter.out(System.out, rsMysql, query);

		
		
		queryString2 = prolog_m + NL + prolog_r + NL + NL + prolog_rdfs + NL +prolog_owl + NL + prolog_e + NL +
		// maladie virale : 
		"SELECT ?facteur ?transmission WHERE {?facteur maladie:MF_NOMFACTEUR ?maladie . ?facteur virus:VIRUS_TRANSMISSION ?transmission}";
		
		System.out.println("Table générale : ");
		query = QueryFactory.create(queryString2) ;
		query.serialize(new IndentedWriter(System.out,true)) ;
        System.out.println() ;
        
		qexecMysql = QueryExecutionFactory.create(query,model_onto) ;

		rsMysql = qexecMysql.execSelect() ;
		System.out.println("Pour Mysql : ");
		ResultSetFormatter.out(System.out, rsMysql, query);
		
		
		
//		try {       
//	    	 FileOutputStream outStream = new FileOutputStream("maladies.owl");
//            // exporte le resultat dans un fichier
//            model_onto.write(outStream, "RDF/XML");
//            outStream.close();
//		  }
//	      catch (FileNotFoundException e) {System.out.println("File not found");}
//	      catch (IOException e) {System.out.println("IO problem");
//	      }
		

	}

}
