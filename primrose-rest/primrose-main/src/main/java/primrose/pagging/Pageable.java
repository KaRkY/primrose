package primrose.pagging;

import primrose.hal.HalResource;

public interface Pageable extends HalResource {

  int count();

}
