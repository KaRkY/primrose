package primrose.accounts;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public final class AccountType {
  private final String code;

  private AccountType(final String code) {
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
  public static AccountType of(final String code) {
    return new AccountType(Objects.requireNonNull(code, "code"));
  }
}
