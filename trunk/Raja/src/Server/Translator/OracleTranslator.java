package Server.Translator;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;

import oracle.jdbc.OracleDatabaseMetaData;
import oracle.jdbc.OracleResultSetMetaData;

import Query.DeleteQuery;
import Query.InsertQuery;
import Query.Pair;
import Query.UpdateQuery;
import Server.DataBase.DataBase;
import Server.DataBase.DataBaseType;


/**
 * This is our oracle translator.
 */
public class OracleTranslator extends Translator 
{
	Connection connexion;
	Statement instruction;

	public OracleTranslator(DataBase dataBase, String n3File, String getMetaInfo, Vector<Pair<String, String>> prefix) 
	{
		super(dataBase, n3File, getMetaInfo, prefix);

		// jdbc:oracle:thin@://localhost:8000:les_maladies
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connexion = (Connection) DriverManager.getConnection("jdbc:"+
					DataBaseType.ORACLE+
					":thin:@"+
					dataBase.getAddress()+
					":"+dataBase.getPort()+":"+dataBase.getDatabaseName(), dataBase.getUserName(), dataBase.getPassWord());

			instruction = (Statement) connexion.createStatement();
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	/**
	 * Execute a insert query.
	 */
	public boolean insert(InsertQuery query) 
	{
		String str = "";
		str += "INSERT INTO ";
		for(String table : query.getFrom()) {
			str += table+", ";
		}
		str += "VALUES (";
		//		for(Pair<String, String> attribut_valeur : query.getValue()) {
		//			str += attribut_valeur.getSecond()+", ";
		//		}
		str += ");";

		System.out.println(str);

		try {

			ResultSet resultat = (ResultSet) instruction.executeQuery(str);

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return false;
	}

	/**
	 * Execute a delete query.
	 */
	public boolean delete(DeleteQuery query) 
	{
		int position_connecteur=0;
		String str = "";

		str += "DELETE FROM ";
		for(String table : query.getFrom()) {
			str += table+", ";
		}
		str += "WHERE ";
		for(Pair<String, String> attribut_valeur : query.getWhere()) {
			str += attribut_valeur.getFirst()+"="+attribut_valeur.getSecond();
			if(position_connecteur<=query.getConnecteur().size()) {
				str += " "+query.getConnecteur().elementAt(position_connecteur);
				position_connecteur++;
			}
		}
		str += ";";

		System.out.println(str);

		try {

			ResultSet resultat = (ResultSet) instruction.executeQuery(str);

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return false;
	}

	/**
	 * Execute a update query.
	 */
	public boolean update(UpdateQuery query) 
	{
		int position_connecteur=0;
		String str = "";

		str += "UPDATE ";
		for(String table : query.getFrom()) {
			str += table+", ";
		}

		str += "SET ";
		for(Pair<String, String> attribut_valeur : query.getSet()) {
			str += attribut_valeur.getFirst()+"="+attribut_valeur.getSecond();
			if(position_connecteur<=query.getConnecteur().size()) {
				str += " "+query.getConnecteur().elementAt(position_connecteur);
				position_connecteur++;
			}
		}

		str += "WHERE ";
		for(Pair<String, String> attribut_valeur : query.getWhere()) {
			str += attribut_valeur.getFirst()+"="+attribut_valeur.getSecond();
		}
		str += ";";

		System.out.println(str);

		try {

			ResultSet resultat = (ResultSet) instruction.executeQuery(str);

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return false;
	}

	public HashMap<String,Vector<String>> getMetaInfoFromDataBase() throws SQLException{
		HashMap<String, Vector<String>> res = new HashMap<String, Vector<String>>();

		Vector<String> res_col = new Vector<String>();

		OracleDatabaseMetaData meta_donnes_tables = (OracleDatabaseMetaData)connexion.getMetaData();
		ResultSet r = meta_donnes_tables.getSchemas();
		while(r.next()){
			String table = r.getString(1);
			if(table.equalsIgnoreCase(database.getUserName()))
			{
				ResultSet rs1 = meta_donnes_tables.getTables(null,r.getString(1),"%",null);
				while(rs1.next()) {
					if(!rs1.getString(3).contains("$"))
					{
						res.put(rs1.getString(3), new Vector<String>());

						Statement stmt = connexion.createStatement();
						ResultSet rs = stmt.executeQuery("SELECT * from "+rs1.getString(3));

						OracleResultSetMetaData meta_donnes_column = (OracleResultSetMetaData)rs.getMetaData();

						int columnCount = meta_donnes_column.getColumnCount();
						for(int i=1; i<=columnCount; i++){
							res.get(rs1.getString(3)).add(meta_donnes_column.getColumnName(i));
						}
					}
				}
			}
		}

		return res;
	}

}
