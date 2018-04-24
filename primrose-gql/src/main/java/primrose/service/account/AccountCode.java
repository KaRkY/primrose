package primrose.service.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountCode {
  @JsonValue
  private final String code;

  @JsonCreator
  public static AccountCode of(String value) {
    return new AccountCode(value);
  }
}
