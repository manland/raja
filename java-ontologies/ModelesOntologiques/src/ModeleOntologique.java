// constructeurs OWL


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.mindswap.pellet.jena.PelletReasonerFactory;

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

		Model model_a_remplir = ModelFactory.createDefaultModel();
		
		// use the file manager to read an RDF document into the model
		Model d2rqModelMaladieFacteur = new ModelD2RQ("file:MaladieFacteurMapping.n3");
		
		Model d2rqModelVirusTransmission = new ModelD2RQ("file:VirusTransmissionMapping.n3");
		
		
		model_onto.add(d2rqModelMaladieFacteur);
		model_onto.add(d2rqModelVirusTransmission);
	
		String str_virus ="http://www.lirmm.fr/virus#";
		String str_maladie="http://www.lirmm.fr/maladie#";

		
		model_onto.setNsPrefix("maladie", str_maladie);
		model_onto.setNsPrefix("virus", str_virus);
		
		// création des classes du modèle ontologique : 
		OntClass maladie = model_onto.createClass(str_maladie+"MALADIE");
		OntClass facteur = model_onto.createClass(str_maladie+"FACTEUR");
		OntClass pathogene = model_onto.createClass(str_maladie+"PATHOGENE");
		OntClass facteurViraux = model_onto.createClass(str_maladie+"FACTEURVIRAUX");
		
		OntClass virus = model_onto.createClass(str_virus+"VIRUS");
		OntClass bacterie =  model_onto.createClass(str_virus+"BACTERIE");
		
		OntClass maladie_virale = model_onto.createClass(str_maladie+"MALADIEVIRALE");
		OntClass maladie_bacterienne = model_onto.createClass(str_maladie+"MALADIEBACTERIENNE");
		
		
		// ajout de l'héritage entre les classes :

		maladie_bacterienne.setSuperClass(maladie);
		maladie_virale.setSuperClass(maladie);
		pathogene.setSuperClass(facteur);
		facteurViraux.setSuperClass(facteur);
		virus.setSuperClass(facteurViraux);
		bacterie.setSuperClass(pathogene);
		facteurViraux.addEquivalentClass(virus);
	//	maladie.addEquivalentClass(maladie_virale);
	
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
		
	    
		// requete : 
		
		String prolog_m = "PREFIX maladie: <"+str_maladie+">" ;
		String prolog_e = "PREFIX virus: <"+str_virus+">" ;
		String prolog_r = "PREFIX rdf: <"+RDF.getURI()+">" ;
		String prolog_rdfs = "PREFIX rdfs: <"+RDFS.getURI()+">" ;
		String prolog_owl = "PREFIX owl: <"+OWL.getURI()+">" ;
		String porlog_d2rq = "PREFIX d2rq: <http://www.wiwiss.fu-berlin.de/suhl/bizer/D2RQ/0.1#>";
		
		//tous les virus du systeme avec leur sourche virale et leur type de genome: 
		String queryString2 = prolog_m + NL + prolog_r + NL +  prolog_rdfs + NL +prolog_owl + NL + prolog_e + NL+ porlog_d2rq + NL+
		"SELECT ?a ?b ?c WHERE{?a ?b ?c}";
		
		System.out.println("Table générale : ");
		Query query = QueryFactory.create(queryString2) ;
		QueryExecution qexecMysql = QueryExecutionFactory.create(query,model_onto) ;

		ResultSet rsMysql = qexecMysql.execSelect() ;
		System.out.println("Pour Mysql : ");
		ResultSetFormatter.out(System.out, rsMysql, query);
		
		queryString2 = prolog_m + NL + prolog_r + NL +  prolog_rdfs + NL +prolog_owl + NL + prolog_e + NL+ porlog_d2rq + NL+
		"SELECT ?a ?ressources ?c WHERE{{?a rdf:type owl:Class . ?a rdf:type rdfs:Class} UNION {?a rdf:type owl:Class . ?b rdfs:subClassOf ?a} }";
//		//UNION {?a a rdf:Property}
		System.out.println("Table générale : ");
		query = QueryFactory.create(queryString2) ;
		qexecMysql = QueryExecutionFactory.create(query,model_onto) ;
		
		System.out.println("Pour Mysql : ");
		ResultSetFormatter.out(System.out, rsMysql, query);

		try {
            // Assumption: it's a SELECT query.
			rsMysql = qexecMysql.execSelect() ;
			
			// a decommenter si tu veux voir le resultat de la requete, mais du coup
			// ca ne remplit pas le modele (ca execute pas ce qu'il y a dessous)
			
		//	ResultSetFormatter.out(System.out, rsMysql, query);
            
			// on entre les prefixes à ajouter au fichier owl
            model_a_remplir.setNsPrefix("rdf", RDF.getURI());
            model_a_remplir.setNsPrefix("rdfs", RDFS.getURI());
            model_a_remplir.setNsPrefix("owl", OWL.getURI());
            model_a_remplir.setNsPrefix("maladie", str_maladie);
            model_a_remplir.setNsPrefix("virus", str_virus);
            
            // pour chaque resultat de la requete on "enregistre" les resultat dans 
            // le modele à remplir : model_a_remplir
            for ( ; rsMysql.hasNext() ; )
            {
                QuerySolution rb = rsMysql.nextSolution() ;
                
                // Get title - variable names do not include the '?'
                RDFNode y = rb.get("a");
                Resource z = (Resource) y;
                Property p = model_a_remplir.createProperty(RDF.getURI()+":type");
                Resource r = model_a_remplir.createResource("owl:Class"); 
                Statement s = model_a_remplir.createStatement(z, p, r);
                
                model_a_remplir.add(s);
             }
         // requetes sur modele à remplir : 
    		queryString2 = prolog_m + NL + prolog_r + NL + NL + prolog_rdfs + NL +prolog_owl + NL + prolog_e + NL +
    		"SELECT ?a ?b ?c WHERE {?a ?b ?c}";
    		//
    		System.out.println("Table générale : ");
    		query = QueryFactory.create(queryString2) ;
    		query.serialize(new IndentedWriter(System.out,true)) ;
            System.out.println() ;
            
    		qexecMysql = QueryExecutionFactory.create(query,model_a_remplir) ;

    		rsMysql = qexecMysql.execSelect() ;
    		System.out.println("Pour Mysql : ");
    		ResultSetFormatter.out(System.out, rsMysql, query);
        }
        finally
        {
            // QueryExecution objects should be closed to free any system resources
            qexecMysql.close() ;
        }
		
        
        
        //  requete qui concerne l'équivalence de classe
        
//		queryString2 = prolog_m + NL + prolog_r + NL + NL + prolog_rdfs + NL +prolog_owl + NL + prolog_e + NL +
//		// maladie virale : 
//		"SELECT ?facteur ?transmission WHERE {?facteur maladie:MF_NOMFACTEUR ?maladie . ?facteur virus:VIRUS_TRANSMISSION ?transmission}";
//		//
//		System.out.println("Table générale : ");
//		query = QueryFactory.create(queryString2) ;
//		query.serialize(new IndentedWriter(System.out,true)) ;
//        System.out.println() ;
//        
//		qexecMysql = QueryExecutionFactory.create(query,model_onto) ;
//
//		rsMysql = qexecMysql.execSelect() ;
//		System.out.println("Pour Mysql : ");
//		ResultSetFormatter.out(System.out, rsMysql, query);
		
		
		
		
		
		// a décommenter pour générer le fichier owl pour le modele créé par remplissages
		
//		try {       
//	    	 FileOutputStream outStream = new FileOutputStream("mal.owl");
//            // exporte le resultat dans un fichier
//            model_a_remplir.write(outStream, "RDF/XML");
//            outStream.close();
//		  }
//	      catch (FileNotFoundException e) {System.out.println("File not found");}
//	      catch (IOException e) {System.out.println("IO problem");
//	      }
		

	}

}
