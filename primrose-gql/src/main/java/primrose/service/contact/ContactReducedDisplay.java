package primrose.service.contact;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ContactReducedDisplay {

  private final long   id;
  @NotBlank
  private final String fullName;
  private final String primaryEmail;
  private final String primaryPhone;

  @JsonCreator
  public ContactReducedDisplay(
    @JsonProperty("id") long id,
    @JsonProperty("fullName") String fullName,
    @JsonProperty("primaryEmail") String primaryEmail,
    @JsonProperty("primaryPhone") String primaryPhone) {
    super();
    this.id = id;
    this.fullName = fullName;
    this.primaryEmail = primaryEmail;
    this.primaryPhone = primaryPhone;
  }

  public long getId() {
    return id;
  }

  public String getFullName() {
    return fullName;
  }

  public String getPrimaryEmail() {
    return primaryEmail;
  }

  public String getPrimaryPhone() {
    return primaryPhone;
  }

}
