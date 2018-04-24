package primrose.service.contact;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ContactCode {
  @JsonValue
  private final String code;

  @JsonCreator
  public static ContactCode of(String value) {
    return new ContactCode(value);
  }
}
