package primrose.contacts;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import org.springframework.stereotype.Component;

import primrose.hal.ImmutableLink;
import primrose.hal.ResourceAssemblerSupport;

@Component
public class ContactResourceAssembler extends ResourceAssemblerSupport<Contact, ContactResource> {

  @Override
  public ContactResource toResource(final Contact domain) {
    return ImmutableContactResource.builder()
      .name(domain.name())
      .email(domain.email())
      .phone(domain.phone())
      .putLink("self", ImmutableLink.builder()
        .href(fromController(ContactsController.class).path("/{id}").buildAndExpand(domain.id()).toUriString())
        .title(domain.name())
        .build())
      .build();
  }

  @Override
  public Contact fromResource(final ContactResource resource) {
    return ImmutableContact.builder()
      .name(resource.name())
      .email(resource.email())
      .phone(resource.phone())
      .build();
  }

}
