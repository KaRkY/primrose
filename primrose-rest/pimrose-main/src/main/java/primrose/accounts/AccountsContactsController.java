package primrose.accounts;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<Map<String, List<Contact>>> search(@PathVariable("account") final String account) {
    return ResponseEntity.ok(contactsRepository.getByAccountUrl(account));
  }
}
