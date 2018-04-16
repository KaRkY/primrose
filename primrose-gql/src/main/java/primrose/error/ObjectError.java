package primrose.error;

public class ObjectError {
  private final String objectName;
  private final String code;
  private final String defaultMessage;

  public ObjectError(String objectName, String code, String defaultMessage) {
    super();
    this.objectName = objectName;
    this.code = code;
    this.defaultMessage = defaultMessage;
  }

  public String getObjectName() {
    return objectName;
  }

  public String getCode() {
    return code;
  }

  public String getDefaultMessage() {
    return defaultMessage;
  }
}
