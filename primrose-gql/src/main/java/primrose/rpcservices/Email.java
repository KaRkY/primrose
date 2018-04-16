package primrose.rpcservices;

import javax.validation.constraints.NotBlank;

public class Email {

  @NotBlank
  private String type;

  @NotBlank
  private String value;

  private Boolean primary;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public Boolean getPrimary() {
    return primary;
  }

  public void setPrimary(Boolean primary) {
    this.primary = primary;
  }
}
