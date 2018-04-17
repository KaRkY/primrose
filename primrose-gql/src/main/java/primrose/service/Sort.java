package primrose.service;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Sort {

  @NotNull
  private final String property;
  @Pattern(regexp = "asc|desc|ASC|DESC")
  private final String direction;

  @JsonCreator
  public Sort(
    @JsonProperty("property") String property,
    @JsonProperty("direction") String direction) {
    super();
    this.property = property;
    this.direction = direction;
  }

  public String getProperty() {
    return property;
  }

  public String getDirection() {
    return direction;
  }

}
