package primrose.contacts;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/metadata/contacts")
public class ContactsMetadataController {

  private final ContactsRepository contactsRepository;

  public ContactsMetadataController(final ContactsRepository contactsRepository) {
    this.contactsRepository = contactsRepository;
  }

  @GetMapping("/types")
  public ResponseEntity<List<ContactType>> getTypes() {
    return ResponseEntity.ok(contactsRepository.getTypes());
  }
}
