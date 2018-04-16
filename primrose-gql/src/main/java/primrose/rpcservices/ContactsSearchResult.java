package primrose.rpcservices;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import primrose.services.ImmutableContactsSearchResult;

@Value.Immutable
@Value.Style(deepImmutablesDetection = true, depluralize = true, throwForInvalidImmutableState = IllegalArgumentException.class)
@JsonDeserialize(as = ImmutableContactsSearchResult.class)
public interface ContactsSearchResult {

  List<ContactSearch> data();

  long count();
}
