package primrose.addresses;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class AddressType {
  private final String code;

  private AddressType(final String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  @Override
  @JsonValue
  public String toString() {
    return code;
  }

  @JsonCreator
  public static AddressType of(final String code) {
    return new AddressType(Objects.requireNonNull(code, "code"));
  }
}
