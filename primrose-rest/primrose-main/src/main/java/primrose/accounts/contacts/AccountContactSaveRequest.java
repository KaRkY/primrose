package primrose.accounts.contacts;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize(as = ImmutableAccountContactSaveRequest.class)
@JsonDeserialize(as = ImmutableAccountContactSaveRequest.class)
public interface AccountContactSaveRequest {

  String name();

  String email();

  String phone();
}
