package primrose.rpcservices;

import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import primrose.services.ImmutableSort;

@Value.Immutable
@Value.Style(deepImmutablesDetection = true, depluralize = true, throwForInvalidImmutableState = IllegalArgumentException.class)
@JsonDeserialize(as = ImmutableSort.class)
public interface Sort {

  @Value.Parameter
  String property();

  @Value.Parameter
  Optional<String> direction();
}
