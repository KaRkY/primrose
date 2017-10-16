package primrose.model;

import java.time.LocalDateTime;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ImplementationVisibility;

@Value.Style(visibility = ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@Value.Immutable
public interface Account extends IdentifiableModel, NamedModel, TypedModel {

  String description();

  String displayName();

  String email();

  String parrentAccountId();

  String phone();

  LocalDateTime validFrom();

  LocalDateTime validTo();

  String website();

  Account withDescription(String description);

  static Builder builder() {
    return new Builder();
  }

  class Builder extends ImmutableAccount.Builder {
  }
}

class Test {
  public static void main(final String[] args) {
    final Account account = Account.builder().build();
  }
}