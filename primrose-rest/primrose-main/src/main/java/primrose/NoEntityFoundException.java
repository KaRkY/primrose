package primrose;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NoEntityFoundException extends PrimroseException {
  private static final long serialVersionUID = 7154685828706011376L;

  public NoEntityFoundException() {
    super();
  }

  public NoEntityFoundException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public NoEntityFoundException(final String message) {
    super(message);
  }

  public NoEntityFoundException(final Throwable cause) {
    super(cause);
  }

}
