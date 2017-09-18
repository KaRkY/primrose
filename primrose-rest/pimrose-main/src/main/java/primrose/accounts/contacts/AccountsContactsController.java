package primrose.accounts.contacts;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Multimap;

import primrose.contacts.Contact;

@RestController
@RequestMapping(path = "/accounts/{account}/contacts")
public class AccountsContactsController {
  private final AccountsContactsRepository accountsContactsRepository;

  public AccountsContactsController(final AccountsContactsRepository accountsContactsRepository) {
    this.accountsContactsRepository = accountsContactsRepository;
  }

  @GetMapping()
  public ResponseEntity<Multimap<String, Contact>> listByAccountCode(@PathVariable("account") final String account) {
    return ResponseEntity.ok(accountsContactsRepository.listByAccountCode(account));
  }
}
