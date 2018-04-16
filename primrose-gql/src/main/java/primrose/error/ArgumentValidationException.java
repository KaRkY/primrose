package primrose.error;

public class ArgumentValidationException extends PrimroseException {
  private static final long serialVersionUID = 559823774984656287L;
  private String[] fields;

  public ArgumentValidationException(String...fields) {
    super(String.format("Missing fields: %s", String.join(" ", fields)));
    this.fields = fields;
  }

  public String[] getFields() {
    return fields;
  }
}
