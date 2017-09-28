package primrose.accounts;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize(as = ImmutableAccountType.class)
@JsonDeserialize(as = ImmutableAccountType.class)
public interface AccountType {
  String code();
  String def();
}
