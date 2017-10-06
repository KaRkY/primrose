package primrose.hal;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.util.UriComponentsBuilder;

public abstract class ResourceAssemblerSupport<D, R extends HalResource> {

  public abstract R toResource(D domain);

  public abstract D fromResource(R resource);

  public abstract UriComponentsBuilder self();

  public List<R> toResource(final Collection<D> domain) {
    return domain
      .stream()
      .map(this::toResource)
      .collect(Collectors.toList());
  }

  public List<D> fromResource(final Collection<R> resources) {
    return resources
      .stream()
      .map(this::fromResource)
      .collect(Collectors.toList());
  }
}
