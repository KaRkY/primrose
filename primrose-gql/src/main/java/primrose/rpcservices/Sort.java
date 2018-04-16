package primrose.rpcservices;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Sort {

  @NotNull
  private String property;
  @Pattern(regexp = "asc|desc|ASC|DESC")
  private String direction;

  public String getProperty() {
    return property;
  }

  public void setProperty(String property) {
    this.property = property;
  }

  public String getDirection() {
    return direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }

}
