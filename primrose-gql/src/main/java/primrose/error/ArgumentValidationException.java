package primrose.error;

import org.springframework.validation.Errors;

public class ArgumentValidationException extends PrimroseException {
   private static final long serialVersionUID = 559823774984656287L;
   private final Errors      errors;

   public ArgumentValidationException(Errors errors) {
      super(errors.toString());
      this.errors = errors;
   }

   public Errors getErrors() {
      return errors;
   }
}
