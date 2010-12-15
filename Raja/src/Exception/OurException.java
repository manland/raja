package Exception;

/**
 * Exception throwed in our system.
 */
public class OurException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7253782518931963609L;
	/**
	 * Reason of the exception.
	 */
	protected String reason;

	/**
	 * Constructor
	 */
	public OurException(String reason) {
		this.reason = reason;
	}

}
