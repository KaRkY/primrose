package primrose.accounts.addresses;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import primrose.addresses.AddressesService;

@RestController
@RequestMapping(path = "/metadata/accounts/addresses")
public class AccountsAddressesMetadataController {

  private final AddressesService addressesService;

  public AccountsAddressesMetadataController(final AddressesService addressesService) {
    this.addressesService = addressesService;
  }

  @GetMapping("/types")
  public ResponseEntity<List<String>> listTypes() {
    return ResponseEntity.ok(addressesService.loadAccountTypes());
  }
}
