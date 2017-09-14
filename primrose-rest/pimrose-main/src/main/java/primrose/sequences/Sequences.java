package primrose.sequences;

public enum Sequences {
  ACCOUNTS("s_account"),
  ADDRESSES("s_address"),
  CONTACTS("s_contact");

  private final String name;

  private Sequences(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
