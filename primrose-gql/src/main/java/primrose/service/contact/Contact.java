package primrose.service.contact;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import primrose.service.Email;
import primrose.service.Phone;

public class Contact {

  private final long           id;
  @NotBlank
  private final String         fullName;
  private final String         description;
  @Valid
  private final List<Email>    emails;
  @Valid
  private final List<Phone>    phones;
  @NotNull
  private final OffsetDateTime validFrom;
  private final OffsetDateTime validTo;

  @JsonCreator
  public Contact(
    @JsonProperty("id") long id,
    @JsonProperty("fullName") String fullName,
    @JsonProperty("description") String description,
    @JsonProperty("emails") List<Email> emails,
    @JsonProperty("phones") List<Phone> phones,
    @JsonProperty("validFrom") OffsetDateTime validFrom,
    @JsonProperty("validTo") OffsetDateTime validTo) {
    super();
    this.id = id;
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

  public String getFullName() {
    return fullName;
  }

  public String getDescription() {
    return description;
  }

  public List<Email> getEmails() {
    return Collections.unmodifiableList(emails);
  }

  public List<Phone> getPhones() {
    return Collections.unmodifiableList(phones);
  }

  public OffsetDateTime getValidFrom() {
    return validFrom;
  }

  public OffsetDateTime getValidTo() {
    return validTo;
  }
}
