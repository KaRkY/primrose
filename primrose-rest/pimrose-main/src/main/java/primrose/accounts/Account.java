package primrose.accounts;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import primrose.addresses.Address;
import primrose.contacts.Contact;

public class Account {
  private long id;
  private String code;
  private Long parentId;
  private String parentName;
  private String type;
  private String displayName;
  private String fullName;
  private String email;
  private String phone;
  private String website;
  private LocalDateTime validFrom;
  private LocalDateTime validTo;
  private Map<String, List<Address>> addresses;
  private Map<String, List<Contact>> contacts;

  public long getId() {
    return id;
  }

  public void setId(final long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(final String code) {
    this.code = code;
  }

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(final Long parentId) {
    this.parentId = parentId;
  }

  public String getParentName() {
    return parentName;
  }

  public void setParentName(final String parentName) {
    this.parentName = parentName;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(final String displayName) {
    this.displayName = displayName;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(final String fullName) {
    this.fullName = fullName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(final String phone) {
    this.phone = phone;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(final String website) {
    this.website = website;
  }

  public LocalDateTime getValidFrom() {
    return validFrom;
  }

  public void setValidFrom(final LocalDateTime validFrom) {
    this.validFrom = validFrom;
  }

  public LocalDateTime getValidTo() {
    return validTo;
  }

  public void setValidTo(final LocalDateTime validTo) {
    this.validTo = validTo;
  }

  public Map<String, List<Address>> getAddresses() {
    return addresses;
  }

  public void setAddresses(final Map<String, List<Address>> addresses) {
    this.addresses = addresses;
  }

  public Map<String, List<Contact>> getContacts() {
    return contacts;
  }

  public void setContacts(final Map<String, List<Contact>> contacts) {
    this.contacts = contacts;
  }

  @Override
  public String toString() {
    final ToStringBuilder builder = new ToStringBuilder(this);
    builder
      .append("id", id)
      .append("code", code)
      .append("parentID", parentId)
      .append("parentName", parentName)
      .append("type", type)
      .append("displayName", displayName)
      .append("fullName", fullName)
      .append("email", email)
      .append("phone", phone)
      .append("website", website)
      .append("validFrom", validFrom)
      .append("validTo", validTo)
      .append("addresses", addresses)
      .append("contacts", contacts);
    return builder.toString();
  }

}
