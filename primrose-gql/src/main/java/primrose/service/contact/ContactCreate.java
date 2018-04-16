package primrose.service.contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import primrose.service.Email;
import primrose.service.Phone;

public class ContactCreate {

  @NotBlank
  private final String      fullName;
  private final String      description;
  @Valid
  private final List<Email> emails;
  @Valid
  private final List<Phone> phones;

  @SuppressWarnings("unused")
  private ContactCreate() {
    this(null, null, Collections.emptyList(), Collections.emptyList());
  }

  public ContactCreate(String fullName, String description, List<Email> emails, List<Phone> phones) {
    super();
    this.fullName = fullName;
    this.description = description;
    this.emails = new ArrayList<>(emails);
    this.phones = new ArrayList<>(phones);
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
