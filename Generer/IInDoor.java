
/**
 * Interface towards user's interface.
 */
interface IInDoor {
  /**
   * Read a message from the user's interface.
   */
  String read() ;

  /**
   * Write a message to the user's interface.
   */
   write(String result) ;

}
class IndoorFile implements IInDoor {
}
