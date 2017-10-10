package primrose.accounts;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import org.springframework.stereotype.Component;

import primrose.hal.ImmutableLink;
import primrose.hal.ResourceAssemblerSupport;

@Component
public class AccountAddressTypeResourceAssembler extends ResourceAssemblerSupport<AccountAddressType, AccountAddressTypeResource> {

  @Override
  public AccountAddressTypeResource toResource(final AccountAddressType domain) {
    return ImmutableAccountAddressTypeResource.builder()
      .name(domain.name())
      .putLink("self", ImmutableLink.builder()
        .href(fromController(AccountAddressTypesController.class).path("/{type}").buildAndExpand(domain.name()).toUriString())
        .title(domain.name())
        .build())
      .build();
  }

  @Override
  public AccountAddressType fromResource(final AccountAddressTypeResource resource) {
    return ImmutableAccountAddressType.builder()
      .name(resource.name())
      .build();
  }

}
