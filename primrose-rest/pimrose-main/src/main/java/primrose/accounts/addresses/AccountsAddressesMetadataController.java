package primrose.accounts.addresses;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/metadata/accounts/addresses")
public class AccountsAddressesMetadataController {

  private final AccountsAddressesRepository accountsAddressesRepository;

  public AccountsAddressesMetadataController(final AccountsAddressesRepository accountsAddressesRepository) {
    this.accountsAddressesRepository = accountsAddressesRepository;
  }

  @GetMapping("/types")
  public ResponseEntity<List<AddressType>> getTypes() {
    return ResponseEntity.ok(accountsAddressesRepository.getTypes());
  }
}
