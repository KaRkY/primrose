package primrose.service;

import javax.validation.constraints.NotBlank;

public class Phone {

  @NotBlank
  private final String type;

  @NotBlank
  private final String  value;
  private final Boolean primary;

  @SuppressWarnings("unused")
  private Phone() {
    this(null, null, null);
  }

  public Phone(String type, String value, Boolean primary) {
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
