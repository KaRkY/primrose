package primrose.accounts;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Multimap;

import primrose.contacts.Contact;
import primrose.contacts.ContactsRepository;

@RestController
@RequestMapping(path = "/accounts/{account}/contacts")
public class AccountsContactsController {
  private final ContactsRepository contactsRepository;

  public AccountsContactsController(final ContactsRepository contactsRepository) {
    this.contactsRepository = contactsRepository;
  }

  @GetMapping()
  public ResponseEntity<Multimap<String, Contact>> search(@PathVariable("account") final String account) {
    return ResponseEntity.ok(contactsRepository.getByAccountCode(account));
  }
}
