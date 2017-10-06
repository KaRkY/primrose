package primrose.addresses;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

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

import primrose.hal.ImmutableLink;
import primrose.pagging.sort.SortUtil;

@RestController
@RequestMapping(path = "/addresses")
public class AddressesControler {
  private final AddressesService addressesService;
  private final AddressResourceAssembler addressResourceAssembler;

  public AddressesControler(
    final AddressesService addressesService,
    final AddressResourceAssembler addressResourceAssembler) {
    this.addressesService = addressesService;
    this.addressResourceAssembler = addressResourceAssembler;
  }

  @GetMapping(
    path = "/{address}",
    produces = "application/vnd.primrose.address.v.1.0+json")
  public ResponseEntity<AddressResource> loadById(
    @PathVariable("address") final String address,
    final HttpServletRequest request) {
    return ResponseEntity.ok(addressResourceAssembler.toResource(addressesService.loadById(address)));
  }

  @PostMapping(
    consumes = "application/vnd.primrose.address.v.1.0+json",
    produces = "application/vnd.primrose.address.v.1.0+json")
  public ResponseEntity<AddressResource> save(
    @RequestBody final AddressResource address,
    final HttpServletRequest request) {
    final Address savedAddress = ImmutableAddress.copyOf(addressesService
      .save(addressResourceAssembler.fromResource(address)))
      .withId(addressesService.getNextId());

    return ResponseEntity
      .created(addressResourceAssembler.self().build().expand(savedAddress.id()).toUri())
      .body(addressResourceAssembler.toResource(savedAddress));
  }

  @PutMapping(
    consumes = "application/vnd.primrose.address.v.1.0+json",
    produces = "application/vnd.primrose.address.v.1.0+json")
  public ResponseEntity<AddressResource> edit(
    @PathVariable("address") final String addressId,
    @RequestBody final AddressResource address) {
    final Address savedAddress = ImmutableAddress.copyOf(addressesService
      .edit(addressId, addressResourceAssembler.fromResource(address)))
      .withId(addressesService.getNextId());

    return ResponseEntity
      .ok(addressResourceAssembler.toResource(savedAddress));
  }

  @GetMapping(
    produces = "application/vnd.primrose.address.v.1.0+json")
  public ResponseEntity<PageableAddressResource> list(
    @RequestParam(required = false) final Integer page,
    @RequestParam(required = false) final Integer size,
    @RequestParam(required = false) final String sort) {
    final ImmutablePageableAddressResource.Builder builder = ImmutablePageableAddressResource.builder();
    final int count = addressesService.count();

    if (page != null && size != null && size * page < count) {
      builder.putLink("next", ImmutableLink.builder()
        .href(fromMethodCall(on(AddressesControler.class).list(page + 1, size, sort)).toUriString())
        .title("Next")
        .build());
    }

    if (page != null && size != null && page > 1) {
      builder.putLink("previous", ImmutableLink.builder()
        .href(fromMethodCall(on(AddressesControler.class).list(page - 1, size, sort)).toUriString())
        .title("Previous")
        .build());
    }

    return ResponseEntity
      .ok(builder
        .count(count)
        .putEmbedded("accounts", addressResourceAssembler
          .toResource(addressesService
            .load(page, size, SortUtil.parseSort(sort))))
        .putLink("self", ImmutableLink.builder()
          .href(fromMethodCall(on(AddressesControler.class).list(page, size, sort)).toUriString())
          .build())
        .build());
  }
}
