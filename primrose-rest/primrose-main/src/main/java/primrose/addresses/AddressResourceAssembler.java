package primrose.addresses;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import primrose.hal.ImmutableLink;
import primrose.hal.ResourceAssemblerSupport;

@Component
public class AddressResourceAssembler extends ResourceAssemblerSupport<Address, AddressResource> {

  @Override
  public AddressResource toResource(final Address address) {
    return ImmutableAddressResource.builder()
      .street(address.street())
      .streetNumber(address.streetNumber())
      .city(address.city())
      .country(address.country())
      .postalCode(address.postalCode())
      .state(address.state())
      .putLink("self", ImmutableLink.builder()
        .href(self().build().expand(address.id()).toUriString())
        .build())
      .build();
  }

  @Override
  public Address fromResource(final AddressResource resource) {
    return ImmutableAddress.builder()
      .street(resource.street())
      .streetNumber(resource.streetNumber())
      .city(resource.city())
      .country(resource.country())
      .postalCode(resource.postalCode())
      .state(resource.state())
      .build();
  }

  @Override
  public UriComponentsBuilder self() {
    return fromController(AddressesControler.class).path("/{id}");
  }

}
