package primrose.error;

public class FieldError {
   private final String objectName;
   private final String field;
   private final String code;
   private final String defaultMessage;
   private final Object rejectedValue;

   public FieldError(String objectName, String field, String code, String defaultMessage, Object rejectedValue) {
      super();
      this.objectName = objectName;
      this.field = field;
      this.code = code;
      this.defaultMessage = defaultMessage;
      this.rejectedValue = rejectedValue;
   }

   public String getObjectName() {
      return objectName;
   }

   public String getField() {
      return field;
   }

   public String getCode() {
      return code;
   }

   public String getDefaultMessage() {
      return defaultMessage;
   }

   public Object getRejectedValue() {
      return rejectedValue;
   }
}
