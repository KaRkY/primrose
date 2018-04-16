package primrose.service;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Sort {

  @NotNull
  private final String property;
  @Pattern(regexp = "asc|desc|ASC|DESC")
  private final String direction;

  @SuppressWarnings("unused")
  private Sort() {
    this(null, null);
  }

  public Sort(String property, String direction) {
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
