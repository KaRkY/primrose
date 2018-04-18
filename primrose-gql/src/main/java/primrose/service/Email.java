package primrose.service;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Email {

  private final long   id;
  @NotBlank
  private final String type;

  @NotBlank
  @javax.validation.constraints.Email
  private final String  value;
  private final Boolean primary;

  @JsonCreator
  public Email(
      @JsonProperty("id") long id,
      @JsonProperty("type") String type,
      @JsonProperty("value") String value,
      @JsonProperty("primary") Boolean primary) {
    super();
    this.id = id;
    this.type = type;
    this.value = value;
    this.primary = primary;
  }

  public long getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public String getValue() {
    return value;
  }

  public Boolean getPrimary() {
    return primary;
  }

}
