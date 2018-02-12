package primrose.types;

import java.util.List;

import org.immutables.value.Value;

@Value.Immutable
public interface TypedAddress {

  String type();

  List<Address> addresses();
}