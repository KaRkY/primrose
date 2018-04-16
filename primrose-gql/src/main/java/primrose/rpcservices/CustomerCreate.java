package primrose.rpcservices;

import java.util.List;
import java.util.Optional;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import primrose.services.ImmutableCustomerCreate;

@Value.Immutable
@Value.Style(
    deepImmutablesDetection = true,
    depluralize = true,
    optionalAcceptNullable = true,
    validationMethod = ValidationMethod.NONE)
@JsonDeserialize(as = ImmutableCustomerCreate.class)
public interface CustomerCreate {

  String type();

  String relationType();

  String fullName();

  Optional<String> displayName();

  Optional<String> description();

  List<Email> emails();

  List<Phone> phones();
}
