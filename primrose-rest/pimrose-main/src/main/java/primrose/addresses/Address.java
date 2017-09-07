package primrose.addresses;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Address {
  private Long id;
  private String code;
  private String street;
  private String city;
  private String postalCode;
  private String state;
  private String country;

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(final String code) {
    this.code = code;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(final String street) {
    this.street = street;
  }

  public String getCity() {
    return city;
  }

  public void setCity(final String city) {
    this.city = city;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(final String postalCode) {
    this.postalCode = postalCode;
  }

  public String getState() {
    return state;
  }

  public void setState(final String state) {
    this.state = state;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(final String country) {
    this.country = country;
  }

  @Override
  public String toString() {
    final ToStringBuilder builder = new ToStringBuilder(this);
    builder
      .append("id", id)
      .append("code", code)
      .append("street", street)
      .append("city", city)
      .append("postalCode", postalCode)
      .append("state", state)
      .append("country", country);
    return builder.toString();
  }

}
