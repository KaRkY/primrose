package primrose.accounts;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/metadata/accounts")
public class AccountsMetadataController {


  private final AccountsService accountsService;

  public AccountsMetadataController(final AccountsService accountsService) {
    this.accountsService = accountsService;
  }

  @GetMapping("/types")
  public ResponseEntity<List<String>> listTypes() {
    return ResponseEntity.ok(accountsService.loadTypes());
  }
}
