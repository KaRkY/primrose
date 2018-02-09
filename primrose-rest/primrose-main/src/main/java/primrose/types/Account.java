package primrose.types;

import org.immutables.value.Value;

@Value.Immutable
public interface Account {

  Long id();

  String name();
}
