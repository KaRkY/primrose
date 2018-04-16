package primrose.rpcservices;

import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import primrose.services.ImmutableContactSearch;

@Value.Immutable
@Value.Style(deepImmutablesDetection = true, depluralize = true, throwForInvalidImmutableState = IllegalArgumentException.class)
@JsonDeserialize(as = ImmutableContactSearch.class)
public interface ContactSearch {

  String fullName();

  String primaryEmail();

  Optional<String> primaryPhone();
}
