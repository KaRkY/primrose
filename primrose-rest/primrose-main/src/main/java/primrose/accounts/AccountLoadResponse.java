package primrose.accounts;

import java.time.LocalDateTime;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize(as = ImmutableAccountLoadResponse.class)
@JsonDeserialize(as = ImmutableAccountLoadResponse.class)
public interface AccountLoadResponse {

  String id();

  String type();

  String parrentAccount();

  String displayName();

  String name();

  String email();

  String phone();

  String website();

  String description();

  LocalDateTime validFrom();

  LocalDateTime validTo();

}
