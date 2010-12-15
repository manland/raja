package Exception;

/**
 * Exception throwed when a bad syntax is releved in a query.
 */
public class MalformedQueryException extends OurException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8750206540772194567L;
	/**
	 * The malformed query.
	 */
	protected String query;

	/**
	 * Contructor
	 */
	public MalformedQueryException(String query, String reason) {
		super(reason);
		this.query = query;
	}

}
