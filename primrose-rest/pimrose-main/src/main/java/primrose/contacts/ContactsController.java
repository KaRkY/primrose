package primrose.contacts;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import primrose.repositories.SearchParameters;
import primrose.util.SearchRequest;
import primrose.util.SearchRequestValidator;
import primrose.util.SearchResponse;

@RestController
@RequestMapping(path = "/contacts")
public class ContactsController {

  private final ContactsService    contactsService;
  private final ContactsRepository contactsRepository;

  public ContactsController(final ContactsService contactsService, final ContactsRepository contactsRepository) {
    this.contactsService = contactsService;
    this.contactsRepository = contactsRepository;
  }

  @InitBinder("searchRequest")
  private void initBinder(final WebDataBinder binder) {
    binder.setValidator(new SearchRequestValidator());
  }

  @GetMapping()
  public ResponseEntity<List<Contact>> search(@Valid final SearchRequest searchRequest) {
    return SearchResponse.of(searchRequest, contactsRepository.search(SearchParameters.of(searchRequest)))
      .buildResponse();
  }
}
