package Server;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;

import Exception.MalformedQueryException;
import Query.IQuery;
import Query.SelectQuery;
import Server.Adapter.CompositeAdapter;
import Server.Adapter.IAdapter;
import Server.Adapter.TerminalAdapter;
import Server.DataBase.DataBase;
import Server.DataBase.DataBaseType;
import Server.Translator.ITranslator;
import Server.Translator.MySqlTranslator;
import Server.Translator.OracleTranslator;
import Server.Translator.PostGreTranslator;

/**
 * Factory used to create instances of objects issued of complex class hierarchy.
 */
public class Factory {
	/**
	 * Create and return Query object matching with the given query.
	 * @throws MalformedQueryException 
	 */
	public static IQuery makeQuery(String query) throws MalformedQueryException
	{
		SelectQuery sq = new SelectQuery();
		sq.setQuery(query);
		sq.parseQuery(query);
		System.out.println("query :" +query);
		return sq;
	}

	/**
	 * Create and return the Traductor matching with the given database.
	 */
	public static ITranslator makeTranslator(DataBase database, String n3File, String getMetaInfo, Vector<String> prefix)
	{
		ITranslator translator = null;
		if(DataBaseType.MYSQL.equals(database.getType()))
		{
			translator = new MySqlTranslator(database, n3File, getMetaInfo, prefix);
		}
		else if(DataBaseType.ORACLE.equals(database.getType()))
		{
			translator = new OracleTranslator(database, n3File, getMetaInfo, prefix);
		}
		else if(DataBaseType.POSTGRE.equals(database.getType()))
		{
			translator = new PostGreTranslator(database, n3File, getMetaInfo, prefix);
		}
		return translator;
	}

	public static DataBase xmlToDataBase(Element element)
	{
		DataBase dataBase = new DataBase();
		dataBase.setAddress(element.getChild("address").getValue());
		dataBase.setDatabaseName(element.getChild("dataBaseName").getValue());
		dataBase.setPassWord(element.getChild("password").getValue());
		dataBase.setPort(Integer.parseInt(element.getChild("port").getValue()));

		if(element.getChild("type").getValue().equals(DataBaseType.MYSQL))
		{
			dataBase.setType(DataBaseType.MYSQL);
		}
		else if(element.getChild("type").getValue().equals(DataBaseType.ORACLE))
		{
			dataBase.setType(DataBaseType.ORACLE);
		}
		else if(element.getChild("type").getValue().equals(DataBaseType.POSTGRE))
		{
			dataBase.setType(DataBaseType.POSTGRE);
		}

		dataBase.setUserName(element.getChild("userName").getValue());
		return dataBase;
	}
	
	public static Vector<IAdapter> xmlToAdapters(List xml)
	{
		Vector<IAdapter> subAdapters = new Vector<IAdapter>();
		Iterator i = xml.iterator();
		while(i.hasNext())
		{
			Element element = (Element)i.next();
			if(element.getName().equals("CompositeAdapter"))
			{
				String owlFile = element.getChild("OWL").getAttributeValue("url");
				Vector<String> prefix = new Vector<String>();
				List e_prefix = element.getChild("Prefix").getChildren("P");
				Iterator i_p = e_prefix.iterator();
				while(i_p.hasNext())
				{
					Element p = (Element)i_p.next();
					prefix.add(p.getValue());
				}
				CompositeAdapter subAdapter = new CompositeAdapter(prefix, owlFile);
				subAdapter.setSubAdapters(xmlToAdapters(element.getChildren()));
				subAdapters.add(subAdapter);
			}
			else if(element.getName().equals("TerminalAdapter"))
			{
				subAdapters.add(xmlToTerminalAdapter(element.getChild("Traducteur")));
			}
		}
		return subAdapters;
	}
	
	public static TerminalAdapter xmlToTerminalAdapter(Element xml) 
	{
		String n3File = xml.getChild("N3").getChild("URL").getValue();
		String getMetaInfo = xml.getChild("N3").getChild("GetMetaInfo").getValue();
		Vector<String> prefix = new Vector<String>();
		List e_prefix = xml.getChild("N3").getChild("Prefix").getChildren("P");
		Iterator i_p = e_prefix.iterator();
		while(i_p.hasNext())
		{
			Element p = (Element)i_p.next();
			prefix.add(p.getValue());
		}
		DataBase dataBase = Factory.xmlToDataBase(xml.getChild("Database"));
		ITranslator translator = Factory.makeTranslator(dataBase, n3File, getMetaInfo, prefix);
		return new TerminalAdapter(prefix, translator);
	}
}

