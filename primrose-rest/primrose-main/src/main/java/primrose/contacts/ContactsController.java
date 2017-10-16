package primrose.contacts;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import primrose.accounts.AccountsController;
import primrose.hal.ImmutableEntity;
import primrose.hal.ImmutableLink;
import primrose.hal.ImmutablePageableResource;
import primrose.hal.PageableResource;
import primrose.pagging.sort.SortUtil;

@RestController
@RequestMapping(path = "/contacts")
public class ContactsController {

  private final ContactsService          contactsService;
  private final ContactResourceAssembler contactResourceAssembler;

  public ContactsController(
    final ContactsService contactsService,
    final ContactResourceAssembler contactResourceAssembler) {
    this.contactsService = contactsService;
    this.contactResourceAssembler = contactResourceAssembler;
  }

  @GetMapping(path = "/{contact}", produces = "application/vnd.primrose.contact.v.1.0+json")
  public ResponseEntity<ContactResource> loadById(
    @PathVariable("contact") final String contact,
    final HttpServletRequest request) {
    return ResponseEntity.ok(contactResourceAssembler.toResource(contactsService.loadById(contact)));
  }

  @PostMapping(consumes = "application/vnd.primrose.contact.v.1.0+json", produces = "application/vnd.primrose.contact.v.1.0+json")
  public ResponseEntity<ContactResource> save(
    @RequestBody final ContactResource contact,
    final HttpServletRequest request) {
    final Contact savedContact = ImmutableContact.copyOf(contactsService
      .save(contactResourceAssembler.fromResource(contact)))
      .withId(contactsService.getNextId());

    return ResponseEntity
      .created(fromController(getClass()).path("/{id}").buildAndExpand(savedContact.id()).toUri())
      .body(contactResourceAssembler.toResource(savedContact));
  }

  @PutMapping(path = "/{contact}", consumes = "application/vnd.primrose.contact.v.1.0+json", produces = "application/vnd.primrose.contact.v.1.0+json")
  public ResponseEntity<ContactResource> edit(
    @PathVariable("contact") final String contactId,
    @RequestBody final ContactResource contact) {
    final Contact savedContact = ImmutableContact.copyOf(contactsService
      .edit(contactId, contactResourceAssembler.fromResource(contact)))
      .withId(contactsService.getNextId());

    return ResponseEntity
      .ok(contactResourceAssembler.toResource(savedContact));
  }

  @GetMapping(produces = "application/vnd.primrose.pageable.v.1.0+json")
  public ResponseEntity<PageableResource> list(
    @RequestParam(required = false) final Integer page,
    @RequestParam(required = false) final Integer size,
    @RequestParam(required = false) final String sort) {
    final List<ContactResource> resource = contactResourceAssembler
      .toResource(contactsService.load(page, size, SortUtil.parseSort(sort)));
    final ImmutablePageableResource.Builder builder = ImmutablePageableResource.builder();
    final int count = contactsService.count();

    final UriComponentsBuilder self = fromController(AccountsController.class)
      .replaceQueryParam("page", page != null && size != null ? new Object[] { page } : null)
      .replaceQueryParam("size", page != null && size != null ? new Object[] { size } : null)
      .replaceQueryParam("sort", sort != null ? new Object[] { sort } : null);

    if (page != null && size != null && size * page < count) {
      builder.putLink("next", ImmutableLink.builder()
        .href(self.cloneBuilder().replaceQueryParam("page", page + 1).toUriString())
        .title("Next")
        .build());
    }

    if (page != null && size != null && page > 1) {
      builder.putLink("previous", ImmutableLink.builder()
        .href(self.cloneBuilder().replaceQueryParam("page", page - 1).toUriString())
        .title("Previous")
        .build());
    }

    return ResponseEntity
      .ok(builder
        .count(count)
        .size(resource.size())
        .addEntity(ImmutableEntity.builder()
          .name("contacts")
          .mediaType("application/vnd.primrose.contact.v.1.0+json")
          .build())
        .putEmbedded("contacts", resource)
        .putLink("self", ImmutableLink.builder().href(self.toUriString()).build())
        .build());
  }
}
