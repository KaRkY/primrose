package primrose.accounts.contacts;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import primrose.contacts.ContactsService;

@RestController
@RequestMapping(path = "/metadata/accounts/contacts")
public class AccountsContactsMetadataController {

  private final ContactsService contactsService;

  public AccountsContactsMetadataController(final ContactsService contactsService) {
    this.contactsService = contactsService;
  }

  @GetMapping("/types")
  public ResponseEntity<List<String>> listTypes() {
    return ResponseEntity.ok(contactsService.loadContactTypes());
  }
}
