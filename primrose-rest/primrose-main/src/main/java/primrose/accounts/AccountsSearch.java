package primrose.accounts;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import primrose.addresses.SearchAddress;
import primrose.contacts.SearchContact;
import primrose.pagging.Pagging;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize(as = ImmutableAccountsSearch.class)
@JsonDeserialize(as = ImmutableAccountsSearch.class)
public interface AccountsSearch extends Pagging {

  SearchAccount account();

  SearchAddress address();

  SearchContact contact();
}
