package Server.Adapter;

import java.util.Vector;

import Exception.DataBaseNotAccessibleException;
import Exception.MalformedQueryException;
import Query.IQuery;
import Query.Pair;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

public abstract class Adapter implements IAdapter {
	
	private Vector<Pair<String, String>> prefix;
	
	private Vector<IListenerAdapter> liste_ecouteurs;
	
	public Adapter(Vector<Pair<String, String>> prefix)
	{
		this.prefix = new Vector<Pair<String, String>>();
		this.prefix.addAll(prefix);
		liste_ecouteurs =  new Vector<IListenerAdapter>();
	}
	
	public Vector<Pair<String, String>> getPrefix()
	{
		return prefix;
	}
	
	@Override
	public abstract Model execute(IQuery query) throws DataBaseNotAccessibleException, MalformedQueryException;

	@Override
	public abstract Model getLocalSchema();

	protected Model execQueryDescribe(String query, Model model) 
	{		
		Model result_model = null;
		Query q = null;
		QueryExecution qexec = null;
		try
		{
			q = QueryFactory.create(query) ;
			qexec = QueryExecutionFactory.create(q, model) ;
			result_model = qexec.execDescribe() ;
		}
		catch (QueryParseException e)
		{
			//System.err.println(e.getMessage());
		}
		return result_model;
	}
	
	protected ResultSet execQuerySelect(String query, Model model) 
	{		
		ResultSet result = null;
		Query q = null;
		QueryExecution qexec = null;
		try
		{
			
			q = QueryFactory.create(query) ;
			qexec = QueryExecutionFactory.create(q,model) ;
			result = qexec.execSelect() ;
		}
		catch (QueryParseException e)
		{
			System.err.println(e.getMessage());
		}
		return result;
	}
	
	public void addListener(IListenerAdapter l){
		liste_ecouteurs.add(l);
	}
	
	public void removeListener(IListenerAdapter l){
		for(int i=0;i<liste_ecouteurs.size();i++){
			if(liste_ecouteurs.get(i).equals(l)){
				liste_ecouteurs.remove(i);
			}
		}
	}
	
	public void fireGoIn(){
		for(int i=0; i<liste_ecouteurs.size();i++){
			liste_ecouteurs.get(i).goIn(this);
		}
	}
	
	public void fireGoOut(){
		for(int i=0;i<liste_ecouteurs.size();i++){
			liste_ecouteurs.get(i).goOut(this);
		}
	}
}
