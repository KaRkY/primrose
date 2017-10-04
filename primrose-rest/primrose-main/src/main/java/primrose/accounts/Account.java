package primrose.accounts;

import java.time.LocalDateTime;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Account {

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
