import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.MinCardinalityRestriction;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.SomeValuesFromRestriction;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import de.fuberlin.wiwiss.d2rq.ModelD2RQ;


public class OntologieVirusMaladie {
	
	public static final String NL = System.getProperty("line.separator");
	public static void main(String[] args) {
		OntModel model_onto = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
		
		String ns_virus ="http://www.lirmm.fr/virus#";
		String ns_maladie="http://www.lirmm.fr/maladie#";

		model_onto.setNsPrefix("maladie", ns_maladie);
		model_onto.setNsPrefix("virus", ns_virus);

		// creation des classes du modèle ontologique :
		OntClass maladie = model_onto.createClass(ns_maladie+"MALADIE");
		OntClass facteur = model_onto.createClass(ns_maladie+"FACTEUR");
		OntClass facteur_viraux = model_onto.createClass(ns_maladie+"FACTEURVIRAUX");
		OntClass facteur_bacterien = model_onto.createClass(ns_maladie+"FACTEURBACTERIENS");
		OntClass maladie_bacterienne = model_onto.createClass(ns_maladie+"MALADIEBACTERIENNE");
		OntClass maladie_virale = model_onto.createClass(ns_maladie+"MALADIEVIRALE");
		OntClass virus = model_onto.createClass(ns_virus+"VIRUS");
		OntClass bacterie = model_onto.createClass(ns_virus+"BACTERIE");

		// ajout de l'héritage entre les classes : 
		facteur_viraux.addSuperClass(facteur);
		facteur_bacterien.addSuperClass(facteur);
		virus.addSuperClass(facteur_viraux);
		bacterie.addSuperClass(facteur_bacterien);
		maladie_virale.addSuperClass(maladie);
		maladie_bacterienne.addSuperClass(maladie);

		// classes équivalentes : 
		virus.addEquivalentClass(facteur_viraux);
		bacterie.addEquivalentClass(facteur_bacterien);

		individusIdentiques(model_onto, facteur_viraux, virus);
		individusIdentiques(model_onto, bacterie, facteur_bacterien);


		// creation de la propriete APrOrigine : 
		ObjectProperty a_pr_origine = model_onto.createObjectProperty(ns_maladie+"APRORIGINE");

		SomeValuesFromRestriction assoc1 = model_onto.createSomeValuesFromRestriction(null, a_pr_origine, virus);
		MinCardinalityRestriction min_card = model_onto.createMinCardinalityRestriction(null, a_pr_origine, 1);
		maladie_virale.addSuperClass(assoc1);
		maladie_virale.addSuperClass(min_card);
	
		SomeValuesFromRestriction assoc2 = model_onto.createSomeValuesFromRestriction(null, a_pr_origine, bacterie);
		maladie_bacterienne.addSuperClass(assoc2);
		maladie_bacterienne.addSuperClass(min_card);

		
		// écriture du modele dans un fichier owl : 
		try {       
			FileOutputStream outStream = new FileOutputStream("maladieVirus.owl");
			// exporte le resultat dans un fichier
			model_onto.write(outStream, "RDF/XML");
			model_onto.write(System.out,"N3");
			outStream.close();
		}
		catch (FileNotFoundException e) {System.out.println("File not found");}
		catch (IOException e) {System.out.println("IO problem");
		}
	}


	public static void individusIdentiques (Model model, OntClass oc1, OntClass oc2)
	{
		String oc1_ns = oc1.getNameSpace();
		String oc2_ns = oc2.getNameSpace(); 

		ResIterator res_i = model.listSubjectsWithProperty( RDF.type, oc1 );
		while (res_i.hasNext())
		{ 
			Resource i1 = res_i.nextResource();

			if (i1.getNameSpace().equals(oc1_ns))
			{ 
				Individual is1 = oc2.createIndividual(oc2_ns + i1.getLocalName());
				is1.setSameAs(i1);
			}	
			else
			{ 
				Individual is1 = oc1.createIndividual(oc1_ns + i1.getLocalName());
				is1.setSameAs(i1);
			}	
		}
	}
}
