package primrose.service;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateEmail {

   @NotBlank
   private final String type;

   @NotBlank
   @javax.validation.constraints.Email
   private final String  value;
   private final Boolean primary;

   @JsonCreator
   public CreateEmail(
      @JsonProperty("type") String type,
      @JsonProperty("value") String value,
      @JsonProperty("primary") Boolean primary) {
      super();
      this.type = type;
      this.value = value;
      this.primary = primary;
   }

   public String getType() {
      return type;
   }

   public String getValue() {
      return value;
   }

   public Boolean getPrimary() {
      return primary;
   }

}
