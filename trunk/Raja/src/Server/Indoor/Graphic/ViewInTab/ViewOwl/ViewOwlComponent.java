package Server.Indoor.Graphic.ViewInTab.ViewOwl;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JLayeredPane;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.util.FileManager;

public class ViewOwlComponent extends JLayeredPane {

	private HashMap<String, EntiteRDF> entites;
	private HashMap<String, Vector<ProprieteRDF>> proprietes;
	
	public ViewOwlComponent()
	{
		super();
		build();
	}
	
	private void build()
	{
		setLayout(null);
		setBackground(new Color(255,255,255));
		setVisible(true);
		setOpaque(true);
		Dimension dimension = new Dimension(800, 600);
		setPreferredSize(dimension);
		setSize(dimension);
		setMinimumSize(dimension);
	}
	
	public void dessine(Model model)
	{
		removeAll();
//		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
//		FileManager.get().readModel(model, "bin/GeneralOnto.owl");
		Query q = QueryFactory.create("SELECT ?a ?b ?c WHERE {?a ?b ?c}") ;
		QueryExecution qexec = QueryExecutionFactory.create(q,model) ;
		ResultSet rs = qexec.execSelect() ;
		QuerySolution o = null;
		int x = -100;
		int y = 0;
		int count = 0;
		entites = new HashMap<String, EntiteRDF>();
		proprietes = new HashMap<String, Vector<ProprieteRDF>>();
		while(rs.hasNext())
		{
			o = rs.nextSolution();
			EntiteRDF entitea = null;
			String uri_entitea = o.get("a").toString();
			if(entites.containsKey(uri_entitea))
			{
				entitea = entites.get(uri_entitea);
			}
			else
			{
				if(x >= 600)
				{
					x = -100;
					y += 100;
					count = 0;
				}
				x += 150;
				entitea = new EntiteRDF(uri_entitea, x, y);
				entites.put(uri_entitea, entitea);
				add(entitea);
				count++;
			}
			EntiteRDF entitec = null;
			String uri_entitec = o.get("c").toString();
			if(entites.containsKey(uri_entitec))
			{
				entitec = entites.get(uri_entitec);
			}
			else
			{
				if(x >= 600)
				{
					x = -100;
					y += 100;
					count = 0;
				}
				x += 150;
				entitec = new EntiteRDF(uri_entitec, x, y);
				entites.put(uri_entitec, entitec);
				add(entitec);
				count++;
			}
			if(entitea != entitec)
			{
				String uri_entiteb = o.get("b").toString();
				ProprieteRDF entiteb = new ProprieteRDF(uri_entiteb, entitea, entitec);
				if(proprietes.containsKey(uri_entiteb))
				{
					proprietes.get(uri_entiteb).add(entiteb);
				}
				else
				{
					Vector<ProprieteRDF> v = new Vector<ProprieteRDF>();
					v.add(entiteb);
					proprietes.put(uri_entiteb, v);
				}
				add(entiteb);
				count++;
			}
			if(y >= getHeight())
			{
				Dimension dimension = new Dimension(800, getHeight()+300);
				setPreferredSize(dimension);
				setSize(dimension);
				setMinimumSize(dimension);
			}
		}
	}
	
	public void dessine2(Model model)
	{
		removeAll();
		NodeIterator ite = model.listObjects();
		while(ite.hasNext())
		{
			System.out.println(ite.nextNode());
		}
		
		
	}
	
	public void lignesDessous()
	{
		for(EntiteRDF rdf : getEntites().values())
		{
			setComponentZOrder(rdf, 0);
			rdf.repaint();
		}
	}
	
	public HashMap<String, EntiteRDF> getEntites() 
	{
		return entites;
	}

	public HashMap<String, Vector<ProprieteRDF>> getProprietes()
	{
		return proprietes;
	}
}
