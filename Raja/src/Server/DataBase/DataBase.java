package Server.DataBase;

/**
 * Classe packaging information on a database.
 */
public class DataBase {
	/**
	 * Ip address or url of database server.
	 */
	protected String address;

	/**
	 * User name used to connect to database.
	 */
	protected String userName;

	/**
	 * Port used to connect to the database.
	 */
	protected int port;

	/**
	 * Type of database.
	 */
	protected String type;

	/**
	 * Password used to connect to database.
	 */
	protected String passWord;

	/**
	 * Name of database.
	 */
	protected String databaseName;

	public DataBase()
	{
		address = "/localhost";
		userName = "root";
		port = -1;
		type = "non renseigné";
		passWord = "";
		databaseName = "les_maladies";
	}
	
	public String getType()
	{
		return type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public void setType(String mysql) {
		this.type = mysql;
	}

}
