package primrose.service.customer;

public class CustomerSearch {

  private final String slug;
  private final String type;
  private final String relationType;
  private final String displayName;
  private final String fullName;
  private final String primaryEmail;
  private final String primaryPhone;

  public CustomerSearch(String slug, String type, String relationType, String displayName, String fullName, String primaryEmail, String primaryPhone) {
    super();
    this.slug = slug;
    this.type = type;
    this.relationType = relationType;
    this.displayName = displayName;
    this.fullName = fullName;
    this.primaryEmail = primaryEmail;
    this.primaryPhone = primaryPhone;
  }

  public String getSlug() {
    return slug;
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
