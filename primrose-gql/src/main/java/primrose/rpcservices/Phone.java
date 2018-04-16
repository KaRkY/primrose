package primrose.rpcservices;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import primrose.services.ImmutablePhone;

@Value.Immutable
@Value.Style(deepImmutablesDetection = true, depluralize = true, throwForInvalidImmutableState = IllegalArgumentException.class)
@JsonDeserialize(as = ImmutablePhone.class)
public interface Phone {

  String type();

  String value();

  boolean primary();
}
