package primrose.users;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize(as = ImmutableLoginUser.class)
@JsonDeserialize(as = ImmutableLoginUser.class)
public interface LoginUser {

  String username();

  String password();
}
