package primrose.accounts;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import org.springframework.stereotype.Component;

import primrose.hal.ImmutableLink;
import primrose.hal.ResourceAssemblerSupport;

@Component
public class AccountTypeResourceAssembler extends ResourceAssemblerSupport<AccountType, AccountTypeResource> {

  @Override
  public AccountTypeResource toResource(final AccountType domain) {
    return ImmutableAccountTypeResource.builder()
      .name(domain.name())
      .putLink("self", ImmutableLink.builder()
        .href(fromController(AccountTypesController.class).path("/{type}").buildAndExpand(domain.name()).toUriString())
        .title(domain.name())
        .build())
      .build();
  }

  @Override
  public AccountType fromResource(final AccountTypeResource resource) {
    return ImmutableAccountType.builder()
      .name(resource.name())
      .build();
  }

}
