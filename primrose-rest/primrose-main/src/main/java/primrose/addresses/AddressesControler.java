package primrose.addresses;

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
@RequestMapping(path = "/addresses")
public class AddressesControler {

  private final AddressesService         addressesService;
  private final AddressResourceAssembler addressResourceAssembler;

  public AddressesControler(
    final AddressesService addressesService,
    final AddressResourceAssembler addressResourceAssembler) {
    this.addressesService = addressesService;
    this.addressResourceAssembler = addressResourceAssembler;
  }

  @GetMapping(path = "/{address}", produces = "application/vnd.primrose.address.v.1.0+json")
  public ResponseEntity<AddressResource> loadById(
    @PathVariable("address") final String address,
    final HttpServletRequest request) {
    return ResponseEntity.ok(addressResourceAssembler.toResource(addressesService.loadById(address)));
  }

  @PostMapping(consumes = "application/vnd.primrose.address.v.1.0+json", produces = "application/vnd.primrose.address.v.1.0+json")
  public ResponseEntity<AddressResource> save(
    @RequestBody final AddressResource address,
    final HttpServletRequest request) {
    final Address savedAddress = ImmutableAddress.copyOf(addressesService
      .save(addressResourceAssembler.fromResource(address)))
      .withId(addressesService.getNextId());

    return ResponseEntity
      .created(fromController(getClass()).path("/{id}").buildAndExpand(savedAddress.id()).toUri())
      .body(addressResourceAssembler.toResource(savedAddress));
  }

  @PutMapping(path = "/{address}", consumes = "application/vnd.primrose.address.v.1.0+json", produces = "application/vnd.primrose.address.v.1.0+json")
  public ResponseEntity<AddressResource> edit(
    @PathVariable("address") final String addressId,
    @RequestBody final AddressResource address) {
    final Address savedAddress = ImmutableAddress.copyOf(addressesService
      .edit(addressId, addressResourceAssembler.fromResource(address)))
      .withId(addressesService.getNextId());

    return ResponseEntity
      .ok(addressResourceAssembler.toResource(savedAddress));
  }

  @GetMapping(produces = "application/vnd.primrose.pageable.v.1.0+json")
  public ResponseEntity<PageableResource> list(
    @RequestParam(required = false) final Integer page,
    @RequestParam(required = false) final Integer size,
    @RequestParam(required = false) final String sort) {
    final List<AddressResource> resource = addressResourceAssembler
      .toResource(addressesService.load(page, size, SortUtil.parseSort(sort)));
    final ImmutablePageableResource.Builder builder = ImmutablePageableResource.builder();
    final int count = addressesService.count();

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
          .name("addresses")
          .mediaType("application/vnd.primrose.address.v.1.0+json")
          .build())
        .putEmbedded("addresses", resource)
        .putLink("self", ImmutableLink.builder().href(self.toUriString()).build())
        .build());
  }
}
