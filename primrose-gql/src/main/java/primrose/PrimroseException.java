package primrose;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class PrimroseException extends RuntimeException {

   private static final long serialVersionUID = -4941176392157106041L;

   public PrimroseException() {
      super();
   }

   public PrimroseException(final String message, final Throwable cause) {
      super(message, cause);
   }

   public PrimroseException(final String message) {
      super(message);
   }

   public PrimroseException(final Throwable cause) {
      super(cause);
   }

}
