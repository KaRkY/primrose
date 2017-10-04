package primrose.accounts.addresses;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import primrose.accounts.AccountsService;
import primrose.addresses.Address;
import primrose.addresses.AddressesService;
import primrose.addresses.ImmutableAddress;

@RestController
@RequestMapping(path = "/accounts/{account}/addresses")
public class AccountsAddressesController {
  private final AccountsService accountsService;
  private final AddressesService addressesService;

  public AccountsAddressesController(final AccountsService accountsService, final AddressesService addressesService) {
    this.accountsService = accountsService;
    this.addressesService = addressesService;
  }

  @GetMapping(
    produces = "application/primrose.account.address.list.response.v.1.0+json")
  public ResponseEntity<Map<String, List<AccountAddressListResponse>>> listByAccount(
    @PathVariable("account") final String account) {
    return ResponseEntity.ok(addressesService
      .loadByAccountId(account)
      .entrySet()
      .stream()
      .collect(Collectors
        .toMap(
          entry -> entry.getKey(),
          entry -> entry.getValue()
            .stream()
            .map(address -> ImmutableAccountAddressListResponse.builder()
              .id(address.id())
              .street(address.street())
              .streetNumber(address.streetNumber())
              .city(address.city())
              .country(address.country())
              .postalCode(address.postalCode())
              .state(address.state())
              .build())
            .collect(Collectors.toList()))));
  }

  @GetMapping(
    path = "/{type}/{address}",
    produces = "application/primrose.account.address.load.response.v.1.0+json")
  public ResponseEntity<AccountAddressLoadResponse> load(
    @PathVariable("account") final String account,
    @PathVariable("type") final String type,
    @PathVariable("address") final String address) {
    final Address loadedAddress = addressesService.loadById(account, type, address);
    return ResponseEntity.ok(ImmutableAccountAddressLoadResponse.builder()
      .id(loadedAddress.id())
      .street(loadedAddress.street())
      .streetNumber(loadedAddress.streetNumber())
      .city(loadedAddress.city())
      .country(loadedAddress.country())
      .postalCode(loadedAddress.postalCode())
      .state(loadedAddress.state())
      .build());
  }

  @PostMapping(
    path = "/{type}",
    consumes = "application/primrose.account.address.save.request.v.1.0+json",
    produces = "application/primrose.account.address.save.response.v.1.0+json")
  public ResponseEntity<AccountAddressSaveResponse> save(
    @PathVariable("account") final String account,
    @PathVariable("type") final String type,
    @RequestBody final AccountAddressSaveRequest address) {
    final Address savedAddress = accountsService.addAddress(
      account,
      ImmutableAddress.builder()
        .id(addressesService.getNextId())
        .street(address.street())
        .streetNumber(address.streetNumber())
        .city(address.city())
        .country(address.country())
        .postalCode(address.postalCode())
        .state(address.state())
        .build(),
      type);
    return ResponseEntity.ok(ImmutableAccountAddressSaveResponse.builder()
      .id(savedAddress.id())
      .street(savedAddress.street())
      .streetNumber(savedAddress.streetNumber())
      .city(savedAddress.city())
      .country(savedAddress.country())
      .postalCode(savedAddress.postalCode())
      .state(savedAddress.state())
      .build());
  }

  @PostMapping(
    path = "/{type}",
    consumes = "application/primrose.account.addressId.save.request.v.1.0+json",
    produces = "application/primrose.account.address.save.response.v.1.0+json")
  public ResponseEntity<AccountAddressSaveResponse> save(
    @PathVariable("account") final String account,
    @PathVariable("type") final String type,
    @RequestBody final AccountAddressIdSaveRequest address) {
    final Address savedAddress = accountsService.addAddress(account, address.id(), type);
    return ResponseEntity.ok(ImmutableAccountAddressSaveResponse.builder()
      .id(savedAddress.id())
      .street(savedAddress.street())
      .streetNumber(savedAddress.streetNumber())
      .city(savedAddress.city())
      .country(savedAddress.country())
      .postalCode(savedAddress.postalCode())
      .state(savedAddress.state())
      .build());
  }
}
