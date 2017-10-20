package primrose.model;

public interface BasePrincipal extends NamedModel {
  String credidentials();

  boolean enabled();

  boolean locked();
}
