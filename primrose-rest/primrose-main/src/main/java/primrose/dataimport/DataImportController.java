package primrose.dataimport;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import primrose.accounts.Account;

@RestController
@RequestMapping(path = "/import")
public class DataImportController {
  private final DataImportService dataImportService;

  public DataImportController(final DataImportService dataImportService) {
    this.dataImportService = dataImportService;
  }

  @PostMapping
  public ResponseEntity<List<Account>> importAccounts(@RequestBody final List<Account> accounts) {
    return ResponseEntity.ok(dataImportService.importAccounts(accounts));
  }
}
