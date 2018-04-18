package primrose.service;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreatePhone {

  @NotBlank
  private final String type;

  @NotBlank
  private final String  value;
  private final Boolean primary;

  @JsonCreator
  public CreatePhone(
    @JsonProperty("type") String type,
    @JsonProperty("value") String value,
    @JsonProperty("primary") Boolean primary) {
    super();
    this.type = type;
    this.value = value;
    this.primary = primary;
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
