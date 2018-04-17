package primrose.service.customer;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerSearch {

  private final long   id;
  @NotBlank
  private final String type;
  @NotBlank
  private final String relationType;
  private final String displayName;
  @NotBlank
  private final String fullName;
  private final String primaryEmail;
  private final String primaryPhone;

  @JsonCreator
  public CustomerSearch(
    @JsonProperty("id") long id,
    @JsonProperty("type") String type,
    @JsonProperty("relationType") String relationType,
    @JsonProperty("displayName") String displayName,
    @JsonProperty("fullName") String fullName,
    @JsonProperty("primaryEmail") String primaryEmail,
    @JsonProperty("primaryPhone") String primaryPhone) {
    super();
    this.id = id;
    this.type = type;
    this.relationType = relationType;
    this.displayName = displayName;
    this.fullName = fullName;
    this.primaryEmail = primaryEmail;
    this.primaryPhone = primaryPhone;
  }

  public long getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public String getRelationType() {
    return relationType;
  }

  public String getDisplayName() {
    return displayName;
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
