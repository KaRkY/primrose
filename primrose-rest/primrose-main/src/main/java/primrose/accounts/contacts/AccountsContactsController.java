package primrose.accounts.contacts;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import primrose.contacts.Contact;
import primrose.contacts.ContactsService;
import primrose.util.Encrypt;

@RestController
@RequestMapping(path = "/accounts/{account}/contacts")
public class AccountsContactsController {

  private final ContactsService contactsService;

  public AccountsContactsController(final ContactsService contactsService) {
    this.contactsService = contactsService;
  }

  @GetMapping()
  public ResponseEntity<Map<String, List<Contact>>> listByAccountCode(@PathVariable("account") final String account) {
    return ResponseEntity.ok(contactsService.loadByAccountId(Encrypt.pseudo(Long.valueOf(account, 36))));
  }
}
