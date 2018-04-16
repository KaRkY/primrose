package primrose.rpcservices;

import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import primrose.services.ImmutableCustomerSearch;

@Value.Immutable
@Value.Style(deepImmutablesDetection = true, depluralize = true, throwForInvalidImmutableState = IllegalArgumentException.class)
@JsonDeserialize(as = ImmutableCustomerSearch.class)
public interface CustomerSearch {

  String type();

  String relationType();

  String fullName();

  Optional<String> displayName();

  String primaryEmail();

  Optional<String> primaryPhone();
}
