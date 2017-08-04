package primrose.accounts;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import primrose.addresses.Address;
import primrose.addresses.AddressesRepository;

@RestController
@RequestMapping(path = "/accounts/{account}/addresses")
public class AccountsAddressesController {
  private final AddressesRepository addressesRepository;

  public AccountsAddressesController(final AddressesRepository addressesRepository) {
    this.addressesRepository = addressesRepository;
  }

  @GetMapping()
  public ResponseEntity<Map<String, List<Address>>> search(@PathVariable("account") final String account) {
    return ResponseEntity.ok(addressesRepository.getByAccountUrl(account));
  }
}
