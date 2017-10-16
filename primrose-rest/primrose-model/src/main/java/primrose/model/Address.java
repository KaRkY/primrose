package primrose.model;

public interface Address extends IdentifiableModel {
  String city();

  String country();

  String postalCode();

  String state();

  String street();

  String streetNumber();
}
