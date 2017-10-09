package primrose.metadata;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import primrose.ImmutableRootResource;
import primrose.RootResource;
import primrose.hal.ImmutableLink;

@RestController
@RequestMapping(path = "/metadata")
public class MetadataController {

  @GetMapping(
    produces = "application/vnd.primrose.metadata.v.1.0+json")
  public ResponseEntity<RootResource> load() {
    return ResponseEntity.ok(ImmutableRootResource.builder()
      .putLink("accounts", ImmutableLink.builder()
        .href(fromController(AccountsMetadataController.class).toUriString())
        .title("Accounts")
        .build())
      .build());
  }
}
