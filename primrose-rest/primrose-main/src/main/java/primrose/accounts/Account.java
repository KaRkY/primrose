package primrose.accounts;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import primrose.addresses.Address;
import primrose.contacts.Contact;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize(as = ImmutableAccount.class)
@JsonDeserialize(as = ImmutableAccount.class)
public interface Account {
  static final String MIME_TYPE = "application/primrose.account.v.1.0+json";

  long id();

  String code();

  String accountType();

  String parrentAccount();

  String displayName();

  String name();

  String email();

  String phone();

  String website();

  String description();

  LocalDateTime validFrom();

  LocalDateTime validTo();

  Map<String, List<Address>> addresses();

  Map<String, List<Contact>> contacts();

}
