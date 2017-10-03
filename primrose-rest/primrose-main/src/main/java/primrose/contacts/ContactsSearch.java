package primrose.contacts;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import primrose.pagging.Pagging;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize(as = ImmutableContactsSearch.class)
@JsonDeserialize(as = ImmutableContactsSearch.class)
public interface ContactsSearch extends Pagging {

  SearchContact contact();

}
