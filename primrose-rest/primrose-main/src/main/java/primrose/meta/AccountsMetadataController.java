package primrose.meta;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/metadata/accounts")
public class AccountsMetadataController {
  private final AccountsMetadataService accountsMetadataService;

  public AccountsMetadataController(final AccountsMetadataService accountsMetadataService) {
    this.accountsMetadataService = accountsMetadataService;
  }

  @GetMapping("/types")
  public ResponseEntity<List<String>> listTypes() {
    return ResponseEntity.ok(accountsMetadataService.loadTypes());
  }

  @GetMapping("/contacts/types")
  public ResponseEntity<List<String>> listContactsTypes() {
    return ResponseEntity.ok(accountsMetadataService.loadContactTypes());
  }

  @GetMapping("/addresses/types")
  public ResponseEntity<List<String>> listAdressTypes() {
    return ResponseEntity.ok(accountsMetadataService.loadAddressTypes());
  }
}
