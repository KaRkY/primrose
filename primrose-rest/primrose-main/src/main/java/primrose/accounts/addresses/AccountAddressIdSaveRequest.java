package primrose.accounts.addresses;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize(as = ImmutableAccountAddressIdSaveRequest.class)
@JsonDeserialize(as = ImmutableAccountAddressIdSaveRequest.class)
public interface AccountAddressIdSaveRequest {

  String id();
}
