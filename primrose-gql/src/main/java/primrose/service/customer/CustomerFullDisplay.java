package primrose.service.customer;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import primrose.service.EmailFullDisplay;
import primrose.service.PhoneFullDisplay;

public class CustomerFullDisplay {

  private final long                   id;
  private final String                 code;
  @NotBlank
  private final String                 type;
  @NotBlank
  private final String                 relationType;
  private final String                 displayName;
  @NotBlank
  private final String                 fullName;
  private final String                 description;
  @Valid
  private final List<EmailFullDisplay> emails;
  @Valid
  private final List<PhoneFullDisplay> phones;
  @NotNull
  private final OffsetDateTime         validFrom;
  private final OffsetDateTime         validTo;

  @JsonCreator
  public CustomerFullDisplay(
    @JsonProperty("id") long id,
    @JsonProperty("code") String code,
    @JsonProperty("type") String type,
    @JsonProperty("relationType") String relationType,
    @JsonProperty("displayName") String displayName,
    @JsonProperty("fullName") String fullName,
    @JsonProperty("description") String description,
    @JsonProperty("emails") List<EmailFullDisplay> emails,
    @JsonProperty("phones") List<PhoneFullDisplay> phones,
    @JsonProperty("validFrom") OffsetDateTime validFrom,
    @JsonProperty("validTo") OffsetDateTime validTo) {
    super();
    this.id = id;
    this.code = code;
    this.type = type;
    this.relationType = relationType;
    this.displayName = displayName;
    this.fullName = fullName;
    this.description = description;
    this.emails = emails != null ? new ArrayList<>(emails) : Collections.emptyList();
    this.phones = phones != null ? new ArrayList<>(phones) : Collections.emptyList();
    this.validFrom = validFrom;
    this.validTo = validTo;
  }

  public long getId() {
    return id;
  }

  public String getCode() {
    return code;
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

  public String getDescription() {
    return description;
  }

  public List<EmailFullDisplay> getEmails() {
    return Collections.unmodifiableList(emails);
  }

  public List<PhoneFullDisplay> getPhones() {
    return Collections.unmodifiableList(phones);
  }

  public OffsetDateTime getValidFrom() {
    return validFrom;
  }

  public OffsetDateTime getValidTo() {
    return validTo;
  }
}