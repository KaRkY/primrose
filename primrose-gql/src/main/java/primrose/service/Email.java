package primrose.service;

import javax.validation.constraints.NotBlank;

public class Email {

  @NotBlank
  private final String type;

  @NotBlank
  private final String  value;
  private final Boolean primary;

  @SuppressWarnings("unused")
  private Email() {
    this(null, null, null);
  }

  public Email(@NotBlank String type, @NotBlank String value, Boolean primary) {
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
