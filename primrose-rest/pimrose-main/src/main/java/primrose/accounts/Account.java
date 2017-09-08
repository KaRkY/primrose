package primrose.accounts;

import java.time.LocalDateTime;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Multimap;

import primrose.addresses.Address;
import primrose.contacts.Contact;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize(as = ImmutableAccount.class)
@JsonDeserialize(as = ImmutableAccount.class)
public interface Account {

  String code();

  AccountType type();

  String displayName();

  String fullName();

  String email();

  String phone();

  String website();

  LocalDateTime validFrom();

  LocalDateTime validTo();

  Multimap<String, Address> addresses();

  Multimap<String, Contact> contacts();

}
