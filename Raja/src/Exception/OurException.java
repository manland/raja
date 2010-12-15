package Exception;

/**
 * Exception throwed in our system.
 */
public class OurException extends Exception {
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
