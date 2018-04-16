package primrose.rpcservices;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import primrose.services.ImmutableEmail;

@Value.Immutable
@Value.Style(deepImmutablesDetection = true, depluralize = true, throwForInvalidImmutableState = IllegalArgumentException.class)
@JsonDeserialize(as = ImmutableEmail.class)
public interface Email {

  String type();

  String value();

  boolean primary();
}
