package primrose.error;

public class PrimroseException extends RuntimeException {
   private static final long serialVersionUID = -2786542883702567942L;

   public PrimroseException() {
      super();
   }

   public PrimroseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
   }

   public PrimroseException(String message, Throwable cause) {
      super(message, cause);
   }

   public PrimroseException(String message) {
      super(message);
   }

   public PrimroseException(Throwable cause) {
      super(cause);
   }

}
