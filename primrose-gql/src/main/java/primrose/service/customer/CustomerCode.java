package primrose.service.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerCode {
  @JsonValue
  private final String code;

  @JsonCreator
  public static CustomerCode of(String value) {
    return new CustomerCode(value);
  }
}
