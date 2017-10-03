package primrose.accounts.addresses;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import primrose.addresses.Address;
import primrose.addresses.AddressesService;
import primrose.util.IdUtil;

@RestController
@RequestMapping(path = "/accounts/{account}/addresses")
public class AccountsAddressesController {
  private final AddressesService addressesService;

  public AccountsAddressesController(final AddressesService addressesService) {
    this.addressesService = addressesService;
  }

  @GetMapping(produces = { "application/primrose.account.address.list.v.1.0+json" })
  public ResponseEntity<Map<String, List<Address>>> listByAccount(@PathVariable("account") final String account) {
    return ResponseEntity.ok(addressesService.loadByAccountId(IdUtil.pseudo(Long.valueOf(account, 36))));
  }

  @GetMapping(path = "/{address}", produces = { "application/primrose.account.address.v.1.0+json" })
  public ResponseEntity<Address> load(@PathVariable("account") final String account,
    @PathVariable("address") final String address) {
    return ResponseEntity.ok(
      addressesService.loadById(
        IdUtil.pseudo(Long.valueOf(account, 36)),
        IdUtil.pseudo(Long.valueOf(address, 36))));
  }
}
