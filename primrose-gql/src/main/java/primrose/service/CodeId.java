package primrose.service;

public class CodeId {
   private final long   id;
   private final String code;

   public CodeId(long id, String code) {
      super();
      this.id = id;
      this.code = code;
   }

   public long getId() {
      return id;
   }

   public String getCode() {
      return code;
   }

}
