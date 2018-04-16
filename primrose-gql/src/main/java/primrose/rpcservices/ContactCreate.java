package primrose.rpcservices;

import java.util.List;
import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import primrose.services.ImmutableCustomerCreate;

@Value.Immutable
@Value.Style(deepImmutablesDetection = true, depluralize = true, throwForInvalidImmutableState = IllegalArgumentException.class)
@JsonDeserialize(as = ImmutableCustomerCreate.class)
public interface ContactCreate {

  String fullName();

  Optional<String> description();

  List<Email> emails();

  List<Phone> phones();
}
