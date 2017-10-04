package primrose.accounts;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import primrose.addresses.AddressSearchParameters;
import primrose.contacts.ContactSearchParameters;
import primrose.pagging.Pagging;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface AccountSearchRequest extends Pagging {

  AccountSearchParameters account();

  AddressSearchParameters address();

  ContactSearchParameters contact();
}
