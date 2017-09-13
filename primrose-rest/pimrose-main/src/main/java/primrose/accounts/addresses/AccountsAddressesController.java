package primrose.accounts.addresses;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Multimap;

import primrose.addresses.Address;

@RestController
@RequestMapping(path = "/accounts/{account}/addresses")
public class AccountsAddressesController {

  private final AccountsAddressesRepository accountsAddressesRepository;

  public AccountsAddressesController(final AccountsAddressesRepository accountsAddressesRepository) {
    this.accountsAddressesRepository = accountsAddressesRepository;
  }

  @GetMapping()
  public ResponseEntity<Multimap<String, Address>> search(@PathVariable("account") final String account) {
    return ResponseEntity.ok(accountsAddressesRepository.getByAccountCode(account));
  }
}
