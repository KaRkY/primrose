package primrose.service;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MetaType {

   @NotBlank
   private final String slug;
   @NotBlank
   private final String name;

   @JsonCreator
   public MetaType(
      @JsonProperty("slug") String slug,
      @JsonProperty("name") String name) {
      super();
      this.slug = slug;
      this.name = name;
   }

   public String getSlug() {
      return slug;
   }

   public String getName() {
      return name;
   }

}
