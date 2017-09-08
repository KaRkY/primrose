package primrose.accounts;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Multimap;

import primrose.addresses.Address;
import primrose.addresses.AddressType;
import primrose.addresses.AddressesRepository;

@RestController
@RequestMapping(path = "/accounts/{account}/addresses")
public class AccountsAddressesController {
  private final AddressesRepository addressesRepository;

  public AccountsAddressesController(final AddressesRepository addressesRepository) {
    this.addressesRepository = addressesRepository;
  }

  @GetMapping()
  public ResponseEntity<Multimap<AddressType, Address>> search(@PathVariable("account") final String account) {
    final Multimap<AddressType, Address> byAccountCode = addressesRepository.getByAccountCode(account);
    System.out.println(byAccountCode);
    return ResponseEntity.ok(byAccountCode);
  }
}
