package Server;

/**
 * Interface towards user's interface.
 */
public interface IInDoor {
  /**
   * Read a message from the user's interface.
   */
  String read() ;

  /**
   * Write a message to the user's interface.
   */
   void write(String result) ;

}
