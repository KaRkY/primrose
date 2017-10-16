package primrose.model;

public interface Contact extends IdentifiableModel, NamedModel {

  String email();

  String phone();
}
