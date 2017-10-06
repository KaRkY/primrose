package primrose.accounts;

import java.time.LocalDateTime;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import primrose.hal.HalResource;

@Value.Immutable()
@Value.Style(
  validationMethod = ValidationMethod.NONE,
  depluralize = true,
  deepImmutablesDetection = true)
@JsonSerialize(as = ImmutableAccountResource.class)
@JsonDeserialize(as = ImmutableAccountResource.class)
public interface AccountResource extends HalResource {

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
