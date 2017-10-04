package primrose.accounts;

import java.time.LocalDateTime;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize(as = ImmutableAccountSearchResponse.class)
@JsonDeserialize(as = ImmutableAccountSearchResponse.class)
public interface AccountSearchResponse {

  String id();

  String type();

  String displayName();

  LocalDateTime validFrom();

  LocalDateTime validTo();

}
