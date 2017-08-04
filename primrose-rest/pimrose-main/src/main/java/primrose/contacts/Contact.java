package primrose.contacts;

import org.apache.commons.lang3.builder.ToStringBuilder;

import primrose.addresses.Address;

public class Contact {
  private long    id;
  private String                     urlCode;
  private String  personName;
  private String  email;
  private String  phone;
  private Address address;

  public long getId() {
    return id;
  }

  public void setId(final long id) {
    this.id = id;
  }

  public String getUrlCode() {
    return urlCode;
  }

  public void setUrlCode(final String urlCode) {
    this.urlCode = urlCode;
  }

  public String getPersonName() {
    return personName;
  }

  public void setPersonName(final String personName) {
    this.personName = personName;
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

  public Address getAddress() {
    return address;
  }

  public void setAddress(final Address address) {
    this.address = address;
  }

  @Override
  public String toString() {
    final ToStringBuilder builder = new ToStringBuilder(this);
    builder
      .append("id", id)
      .append("urlCode", urlCode)
      .append("personName", personName)
      .append("email", email)
      .append("phone", phone)
      .append("address", address);
    return builder.toString();
  }
}
