package primrose.accounts;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import org.springframework.stereotype.Component;

import primrose.hal.ImmutableLink;
import primrose.hal.ResourceAssemblerSupport;

@Component
public class AccountContactTypeResourceAssembler extends ResourceAssemblerSupport<AccountContactType, AccountContactTypeResource> {

  @Override
  public AccountContactTypeResource toResource(final AccountContactType domain) {
    return ImmutableAccountContactTypeResource.builder()
      .name(domain.name())
      .putLink("self", ImmutableLink.builder()
        .href(fromController(AccountContactTypesController.class).path("/{type}").buildAndExpand(domain.name()).toUriString())
        .title(domain.name())
        .build())
      .build();
  }

  @Override
  public AccountContactType fromResource(final AccountContactTypeResource resource) {
    return ImmutableAccountContactType.builder()
      .name(resource.name())
      .build();
  }

}
