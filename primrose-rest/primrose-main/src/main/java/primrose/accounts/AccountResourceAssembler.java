package primrose.accounts;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;

import primrose.hal.ImmutableLink;
import primrose.hal.ResourceAssemblerSupport;

@Component
public class AccountResourceAssembler extends ResourceAssemblerSupport<Account, AccountResource> {

  private final PathMatcher pathMatcher;

  public AccountResourceAssembler(final PathMatcher pathMatcher) {
    this.pathMatcher = pathMatcher;
  }

  @Override
  public AccountResource toResource(final Account account) {
    return ImmutableAccountResource.builder()
      .name(account.name())
      .displayName(account.displayName())
      .description(account.description())
      .email(account.email())
      .phone(account.phone())
      .type(ImmutableLink.builder()
        .href(fromController(AccountTypesController.class).path("/{type}").buildAndExpand(account.type()).toUriString())
        .title(account.type())
        .build())
      .validFrom(account.validFrom())
      .validTo(account.validTo())
      .putLink("self", ImmutableLink.builder()
        .href(fromController(AccountsController.class).path("/{id}").buildAndExpand(account.id()).toUriString())
        .title(account.displayName())
        .build())
      .putLink("addresses", ImmutableLink.builder()
        .href(fromController(AccountAddressesController.class).buildAndExpand(account.id()).toUriString())
        .title("Addresses")
        .build())
      .build();
  }

  @Override
  public Account fromResource(final AccountResource resource) {
    if (resource.type() == null) { throw new IllegalArgumentException("Type is required parameter."); }

    final String pattern = fromController(AccountTypesController.class)
      .path("/{type}")
      .build()
      .toUriString();

    if (!pathMatcher.match(pattern, resource.type().href())) { throw new IllegalArgumentException(
      String.format("Type must be valid URL to account types. Pattern: %s", pattern)); }

    final Map<String, String> variables = pathMatcher
      .extractUriTemplateVariables(pattern, resource.type().href());

    return ImmutableAccount.builder()
      .name(resource.name())
      .displayName(resource.displayName())
      .description(resource.description())
      .email(resource.email())
      .phone(resource.phone())
      .type(variables.get("type"))
      .build();
  }
}
