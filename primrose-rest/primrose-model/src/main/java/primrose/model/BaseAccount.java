package primrose.model;

import java.time.LocalDateTime;

public interface BaseAccount extends TypedModel, NamedModel {

  String displayName();

  String email();

  String phone();

  String website();

  String description();

  LocalDateTime validFrom();

  LocalDateTime validTo();

}