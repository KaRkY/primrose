package primrose.services;

import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Value.Immutable
@JsonDeserialize(as = ImmutableCustomersSearchResult.class)
public interface CustomersSearchResult {

  String type();

  String relationType();

  String fullName();

  Optional<String> displayName();

  String primaryEmail();

  Optional<String> primaryPhone();
}
