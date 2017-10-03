package primrose.pagging;

import primrose.pagging.sort.Sort;

public interface Pagging {

  Integer page();

  Integer size();

  Sort sort();
}
