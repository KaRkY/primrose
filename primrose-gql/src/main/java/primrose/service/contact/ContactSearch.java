package primrose.service.contact;

public class ContactSearch {

  private final long   id;
  private final String fullName;
  private final String primaryEmail;
  private final String primaryPhone;

  public ContactSearch(long id, String fullName, String primaryEmail, String primaryPhone) {
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
