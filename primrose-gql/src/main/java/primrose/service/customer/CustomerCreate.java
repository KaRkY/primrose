package primrose.service.customer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import primrose.service.Email;
import primrose.service.Phone;

public class CustomerCreate {

  @NotBlank
  private final String      type;
  @NotBlank
  private final String      relationType;
  private final String      displayName;
  @NotBlank
  private final String      fullName;
  private final String      description;
  @Valid
  private final List<Email> emails;
  @Valid
  private final List<Phone> phones;

  @SuppressWarnings("unused")
  private CustomerCreate() {
    this(null, null, null, null, null, Collections.emptyList(), Collections.emptyList());
  }

  public CustomerCreate(String type, String relationType, String displayName, String fullName, String description, List<Email> emails, List<Phone> phones) {
    super();
    this.type = type;
    this.relationType = relationType;
    this.displayName = displayName;
    this.fullName = fullName;
    this.description = description;
    this.emails = new ArrayList<>(emails);
    this.phones = new ArrayList<>(phones);
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

  public List<Email> getEmails() {
    return Collections.unmodifiableList(emails);
  }

  public List<Phone> getPhones() {
    return Collections.unmodifiableList(phones);
  }

}
