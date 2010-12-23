import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;


public class OntologieGlobale {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		OntModel model_onto_global = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);


		String ns_virus ="http://www.lirmm.fr/virus#";
		String ns_maladie="http://www.lirmm.fr/maladie#";
		String ns_recensement="http://www.lirmm.fr/recensement#";

		FileManager.get().readModel( model_onto_global, "maladieVirus.owl");
		
		model_onto_global.setNsPrefix("maladie", ns_maladie);
		model_onto_global.setNsPrefix("virus", ns_virus);
		model_onto_global.setNsPrefix("recensement", ns_recensement);

		OntClass maladie = model_onto_global.createClass(ns_maladie +"MALADIE");
		OntClass maladie_rec = model_onto_global.createClass(ns_recensement+ "MALADIE");
		
		maladie.setEquivalentClass(maladie_rec);
		
		// écriture du modele dans un fichier owl : 
		try {       
			FileOutputStream outStream = new FileOutputStream("modeleGlobal.owl");
			// exporte le resultat dans un fichier
			model_onto_global.write(outStream, "RDF/XML");
			model_onto_global.write(System.out,"N3");
			outStream.close();
		}
		catch (FileNotFoundException e) {System.out.println("File not found");}
		catch (IOException e) {System.out.println("IO problem");
		}
		

	}

}
