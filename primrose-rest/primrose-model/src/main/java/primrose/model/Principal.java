package primrose.model;

import java.util.List;

public interface Principal extends IdentifiableModel, NamedModel {
  String credidentials();

  boolean enabled();

  boolean locked();

  List<String> permissions();
}
