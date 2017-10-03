package primrose.accounts;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize(as = ImmutableSearchAccount.class)
@JsonDeserialize(as = ImmutableSearchAccount.class)
public interface SearchAccount {

  String type();

  String parrentAccount();

  String displayName();

  String name();

  String email();

  String phone();

  String website();

  String description();

}
