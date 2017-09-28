package primrose.contacts;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/contacts")
public class ContactsController {

  private final ContactsService contactsService;
  private final ContactsRepository contactsRepository;

  public ContactsController(final ContactsService contactsService, final ContactsRepository contactsRepository) {
    this.contactsService = contactsService;
    this.contactsRepository = contactsRepository;
  }

}
