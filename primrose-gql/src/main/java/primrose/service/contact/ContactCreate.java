package primrose.service.contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import primrose.service.CreateEmail;
import primrose.service.CreatePhone;

public class ContactCreate {

   @NotBlank
   private final String            fullName;
   private final String            description;
   @Valid
   private final List<CreateEmail> emails;
   @Valid
   private final List<CreatePhone> phones;

   @JsonCreator
   public ContactCreate(
      @JsonProperty("fullName") String fullName,
      @JsonProperty("description") String description,
      @JsonProperty("emails") List<CreateEmail> emails,
      @JsonProperty("phones") List<CreatePhone> phones) {
      super();
      this.fullName = fullName;
      this.description = description;
      this.emails = emails != null ? new ArrayList<>(emails) : Collections.emptyList();
      this.phones = phones != null ? new ArrayList<>(phones) : Collections.emptyList();
   }

   public String getFullName() {
      return fullName;
   }

   public String getDescription() {
      return description;
   }

   public List<CreateEmail> getEmails() {
      return Collections.unmodifiableList(emails);
   }

   public List<CreatePhone> getPhones() {
      return Collections.unmodifiableList(phones);
   }

}
