package primrose.contacts;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class ContactType {
  private final String code;

  private ContactType(final String code) {
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
  public static ContactType of(final String code) {
    return new ContactType(Objects.requireNonNull(code, "code"));
  }
}
