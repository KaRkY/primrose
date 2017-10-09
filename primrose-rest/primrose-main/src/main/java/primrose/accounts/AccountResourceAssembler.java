package primrose.accounts;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import primrose.hal.ImmutableLink;
import primrose.hal.ResourceAssemblerSupport;

@Component
public class AccountResourceAssembler extends ResourceAssemblerSupport<Account, AccountResource> {

  @Override
  public UriComponentsBuilder self() {
    return fromController(AccountsController.class).path("/{id}");
  }

  @Override
  public AccountResource toResource(final Account account) {
    return ImmutableAccountResource.builder()
      .name(account.name())
      .displayName(account.displayName())
      .description(account.description())
      .email(account.email())
      .phone(account.phone())
      .type(account.type())
      .validFrom(account.validFrom())
      .validTo(account.validTo())
      .putLink("self", ImmutableLink.builder()
               .href(self().build().expand(account.id()).toUriString())
               .title(account.displayName())
               .build())
      .putLink("addresses", ImmutableLink.builder()
               .href(self().path("/addresses").build().expand(account.id()).toUriString())
               .title("Addresses")
               .build())
      .build();
  }

  @Override
  public Account fromResource(final AccountResource resource) {
    return ImmutableAccount.builder()
      .name(resource.name())
      .displayName(resource.displayName())
      .description(resource.description())
      .email(resource.email())
      .phone(resource.phone())
      .type(resource.type())
      .build();
  }
}
