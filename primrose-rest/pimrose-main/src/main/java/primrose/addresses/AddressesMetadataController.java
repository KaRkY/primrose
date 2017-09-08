package primrose.addresses;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/metadata/addresses")
public class AddressesMetadataController {

  private final AddressesRepository addressesRepository;

  public AddressesMetadataController(final AddressesRepository addressesRepository) {
    this.addressesRepository = addressesRepository;
  }

  @GetMapping("/types")
  public ResponseEntity<List<AddressType>> getTypes() {
    return ResponseEntity.ok(addressesRepository.getTypes());
  }
}
